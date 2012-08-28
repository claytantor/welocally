package com.welocally.admin.spreadsheet.template;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"xmlns","xmlns$openSearch","xmlns$openSearch","xmlns$gs","openSearch$totalResults","openSearch$startIndex"})
public class Feed {
    
        
        List<Author> author;
        
        List<Link> link;
        
        private List<Category> category;
        
        private StringValue id;
        
        private StringValue updated;
              
        private Title title;
        
        

        public List<Category> getCategory() {
            return category;
        }

        public void setCategory(List<Category> category) {
            this.category = category;
        }

        public StringValue getId() {
            return id;
        }

        public void setId(StringValue id) {
            this.id = id;
        }

        public Title getTitle() {
            return title;
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public List<Author> getAuthor() {
            return author;
        }

        public void setAuthor(List<Author> author) {
            this.author = author;
        }

        public StringValue getUpdated() {
            return updated;
        }

        public void setUpdated(StringValue updated) {
            this.updated = updated;
        }

        public List<Link> getLink() {
            return link;
        }

        public void setLink(List<Link> link) {
            this.link = link;
        }
        
        
       
}
