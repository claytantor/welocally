package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.ArticleService;
import com.sightlyinc.ratecred.service.PublisherService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("/publisher/article")
public class ArticleController {

    static Logger logger = Logger.getLogger(ArticleController.class);


    @Autowired
    private ArticleService articleService;
    
	@Autowired
	private PublisherService publisherService;

	
    @RequestMapping(method= RequestMethod.GET)
    public String addArticle(@RequestParam Long publisherId, Model model) {
    	Publisher publisher = publisherService.findByPrimaryKey(publisherId);
		model.addAttribute("publisher", publisher);
		Article article = new Article();
		article.setPublisher(publisher);
        model.addAttribute("articleForm",article);
        return "article/edit";
    }

    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editArticle(@PathVariable Long id, Model model) {
        logger.debug("edit");
        Article a = articleService.findByPrimaryKey(id);
        model.addAttribute("publisher", a.getPublisher());
        model.addAttribute("articleForm", a);
        return "article/edit";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String saveArticle(@Valid Article article) {
        logger.debug("got post action");

        Long id = articleService.save(article);
        return "redirect:/publisher/article/"+id.toString();

    }
	
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public String getArticleById(@PathVariable Long id, Model model) {
        logger.debug("view");
        Article a = articleService.findByPrimaryKey(id);
        model.addAttribute("publisher", a.getPublisher());
        model.addAttribute("article", a);
        return "article/view";
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
    public String deleteArticle(@PathVariable Long id) {
        logger.debug("delete");
        Article article = articleService.findByPrimaryKey(id);
        articleService.delete(article);
        return "redirect:/publisher/article/list";
    }
	
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(@RequestParam Long publisherId, Model model) {
        logger.debug("list");
        Publisher publisher = publisherService.findByPrimaryKey(publisherId);
		model.addAttribute("publisher", publisher);
        model.addAttribute("articles", publisher.getArticles());
        return "article/list";
    }
}
