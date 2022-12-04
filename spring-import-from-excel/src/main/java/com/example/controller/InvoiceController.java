package com.example.controller;

import com.example.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.DateTimeException;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String listPage(Model model) {
        model.addAttribute("invoices", invoiceService.findAll());
        return "invoice";
    }

    @PostMapping("/import")
    public String importFromExcel(@RequestParam("file") MultipartFile multipartFile) {
        logger.info("Importing file {}", multipartFile.getOriginalFilename());
        if (!multipartFile.isEmpty()) {
            try {
                invoiceService.importFromExcel(multipartFile.getInputStream());
            } catch (IOException ex) {
                logger.error("Error with import", ex);
                throw new WebException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (DateTimeException ex) {
                logger.error("Error with import", ex);
                throw new WebException(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return "redirect:/invoice";
    }

    @ExceptionHandler
    public ModelAndView webExceptionHandler(WebException exception) {
        ModelAndView modelAndView = new ModelAndView("error", exception.getStatus());
        modelAndView.addObject("errorMessage", exception.getMessage());
        return modelAndView;
    }
}
