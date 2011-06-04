package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.service.ArticleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/article")
public class ArticleController {

    static Logger logger = Logger.getLogger(ArticleController.class);


    @Autowired
    private ArticleService articleService;

	
    @RequestMapping(method= RequestMethod.GET)
    public String addArticle(Model model) {
        model.addAttribute("articleForm",new Article());
        return "article/edit";
    }

    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editArticle(@PathVariable Long id, Model model) {
        logger.debug("edit");
        model.addAttribute("articleForm", articleService.findByPrimaryKey(id));
        return "article/edit";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String saveArticle(@Valid Article article) {
        logger.debug("got post action");

        Long id = articleService.save(article);
        return "redirect:/admin/article/"+id.toString();

    }
	
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public String getArticleById(@PathVariable Long id, Model model) {
        logger.debug("view");
        model.addAttribute("article", articleService.findByPrimaryKey(id));
        return "article/view";
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
    public String deleteArticle(@PathVariable Long id) {
        logger.debug("delete");
        Article article = articleService.findByPrimaryKey(id);
        articleService.delete(article);
        return "redirect:/admin/article/list";
    }
	
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(Model model) {
        logger.debug("list");
        model.addAttribute("articles", articleService.findAll());
        return "article/list";
    }
}
