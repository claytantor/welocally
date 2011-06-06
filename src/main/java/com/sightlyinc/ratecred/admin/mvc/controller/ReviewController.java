package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.model.Review;
import com.sightlyinc.ratecred.service.ReviewService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("publisher/review")
public class ReviewController {

    static Logger logger = Logger.getLogger(ReviewController.class);


    @Autowired
    private ReviewService reviewService;

	
    @RequestMapping(method= RequestMethod.GET)
    public String addReview(Model model) {
        model.addAttribute("reviewForm",new Review());
        return "review/edit";
    }

    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editReview(@PathVariable Long id, Model model) {
        logger.debug("edit");
        model.addAttribute("reviewForm", reviewService.findByPrimaryKey(id));
        return "review/edit";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String saveReview(@Valid Review review) {
        logger.debug("got post action");

        Long id = reviewService.save(review);
        return "redirect:/publisher/review/"+id.toString();

    }
	
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public String getReviewById(@PathVariable Long id, Model model) {
        logger.debug("view");
        model.addAttribute("review", reviewService.findByPrimaryKey(id));
        return "review/view";
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
    public String deleteReview(@PathVariable Long id) {
        logger.debug("delete");
        Review review = reviewService.findByPrimaryKey(id);
        reviewService.delete(review);
        return "redirect:/publisher/review/list";
    }
	
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(Model model) {
        logger.debug("list");
        model.addAttribute("reviews", reviewService.findAll());
        return "review/list";
    }
}
