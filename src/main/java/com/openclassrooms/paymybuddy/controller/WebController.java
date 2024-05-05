package com.openclassrooms.paymybuddy.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.openclassrooms.paymybuddy.DTO.TransactionDTO;
import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.model.FriendRelationShip;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.service.DBUserService;
import com.openclassrooms.paymybuddy.service.FriendRelationShipService;
import com.openclassrooms.paymybuddy.service.TransactionService;

@Controller
public class WebController {

	private static final Logger logger = LogManager.getLogger("webController");

	@Autowired
	private FriendRelationShipService friendRelationShipService;

	@Autowired
	private DBUserService dbuserService;

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/login")
	public String loginPage() {

		logger.info("get request at /login");

		return "login";
	}

	@GetMapping("/transfert")
	public ModelAndView transfertPage(@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) throws Exception {

		logger.info("get request at /transfert");

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);

		DBUser dbuser = dbuserService.getCurrentUser();
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions = dbuser.getTransactions();
		List<FriendRelationShip> friends = new ArrayList<FriendRelationShip>();
		friends = dbuser.getFriends();

		List<String> friendList = new ArrayList<>();

		for (FriendRelationShip friend : friends) {
			friendList.add(dbuserService.findNicknameById(friend.getFriend_id()));
		}

		String viewName = "transfert";

		List<TransactionDTO> transactionsDTO = new ArrayList<TransactionDTO>();

		for (Transaction transaction : transactions) {
			TransactionDTO transactionDTO = new TransactionDTO(
					dbuserService.findNicknameById(transaction.getReceiverId()), transaction.getDescription(),
					transaction.getAmount());
			transactionsDTO.add(transactionDTO);
		}
		Collections.reverse(transactionsDTO);
		Page<TransactionDTO> transactionsDTOPage = transactionService
				.findPaginated(PageRequest.of(currentPage - 1, pageSize), transactionsDTO);
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("credit", dbuser.getBalance());
		// model.put("transactionsDTO", transactionsDTO);
		model.put("transactionsDTOPage", transactionsDTOPage);
		model.put("friends", friendList);
		model.put("currentPage", currentPage); // add pagination

		int totalPages = transactionsDTOPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.put("pageNumbers", pageNumbers);
		}
		model.put("totalPages", totalPages); // add pagination

		return new ModelAndView(viewName, model);
	}

	@GetMapping("/")
	public ModelAndView rememberMeResultPage() {
		logger.info("get request at /");
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/transfert");

		return new ModelAndView(redirectView);
	}

	@GetMapping("/contact")
	public ModelAndView contactPage() {
		logger.info("get request at /contact");
		String viewName = "contact";

		Map<String, Object> model = new HashMap<String, Object>();

		return new ModelAndView(viewName, model);
	}

	@PostMapping("/contact")
	public ModelAndView addContact(@RequestParam String connection) throws Exception {

		logger.info("post request at /contact");
		logger.info("Create a new Friend RelationShip in database");

		DBUser dbuser = dbuserService.getCurrentUser();
		System.out.println("user connected: " + dbuser.getEmail());

		try {
			friendRelationShipService.createFriendRelationShip(dbuser.getEmail(), connection);
		} catch (Exception e) {
			Map<String, Object> model = new HashMap<String, Object>();

			model.put("errorMessage", "this relationship can't be added!");
			String viewname = "contact";
			return new ModelAndView(viewname, model);
		}

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/transfert");

		return new ModelAndView(redirectView);
	}

	@PostMapping("/transfer")
	public ModelAndView addtransfer(@RequestParam String connection, @RequestParam String amount,
			@RequestParam String description, @RequestParam("page") Optional<Integer> page)
			throws NumberFormatException, Exception {

		logger.info("post request at /transfert");

		DBUser dbuser = dbuserService.getCurrentUser();
		int currentPage = page.orElse(1);
		int pageSize = 3;

		try {
			transactionService.createTransaction(dbuser.getEmail(), dbuserService.findIdByNickName(connection),
					Double.parseDouble(amount), false, description);
		} catch (Exception e) {
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions = dbuser.getTransactions();
			List<FriendRelationShip> friends = new ArrayList<FriendRelationShip>();
			friends = dbuser.getFriends();

			List<String> friendList = new ArrayList<>();

			for (FriendRelationShip friend : friends) {
				friendList.add(dbuserService.findNicknameById(friend.getFriend_id()));
			}

			String viewName = "transfert";

			List<TransactionDTO> transactionsDTO = new ArrayList<TransactionDTO>();

			for (Transaction transaction : transactions) {
				TransactionDTO transactionDTO = new TransactionDTO(
						dbuserService.findNicknameById(transaction.getReceiverId()), transaction.getDescription(),
						transaction.getAmount());
				transactionsDTO.add(transactionDTO);
			}

			Collections.reverse(transactionsDTO);
			Page<TransactionDTO> transactionsDTOPage = transactionService
					.findPaginated(PageRequest.of(currentPage - 1, pageSize), transactionsDTO);
			Map<String, Object> model = new HashMap<String, Object>();

			model.put("credit", dbuser.getBalance());
			model.put("errorMessage", "error, transaction failed!");
			model.put("transactionsDTOPage", transactionsDTOPage);
			model.put("friends", friendList);
			model.put("currentPage", currentPage); // add pagination

			int totalPages = transactionsDTOPage.getTotalPages();

			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.put("pageNumbers", pageNumbers);
			}
			model.put("totalPages", totalPages); // add pagination

			return new ModelAndView(viewName, model);
		}

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/transfert");

		return new ModelAndView(redirectView);
	}
}
