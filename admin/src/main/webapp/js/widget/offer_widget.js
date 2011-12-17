// TODO - add full support for the "open graph" meta elements
if (!window.RATECRED) {
        window.RATECRED = {
                util: {
                        update: function() {
                                var obj = arguments[0], i = 1, len=arguments.length, attr;
                                for (; i<len; i++) {
                                        for (attr in arguments[i]) {
                                                obj[attr] = arguments[i][attr];
                                        }
                                }
                                return obj;
                        },
                        escape: function(s) {
                                return ((s == null) ? '' : s)
                                        .toString()
                                        .replace(/[<>"&\\]/g, function(s) {
                                                switch(s) {
                                                        case '<': return '&lt;';
                                                        case '>': return '&gt;';
                                                        case '"': return '\"';
                                                        case '&': return '&amp;';
                                                        case '\\': return '\\\\';
                                                        default: return s;
                                                }
                                        });
                        },
                        notundef: function(a, b) {
                                return typeof(a) == 'undefined' ? b : a;
                        }
                }
        };
}

if (!RATECRED.OfferWidget) {
        RATECRED.OfferWidget = function(cfg) {
                // Params for cfg
                //   hostname - the name of the host to use 
                //   header -- the header text (or none) to give the widget (default 'Like this:')
                //   s -- array -- list of sites to share to (has a default)
                //   css -- string (or false) -- url for the css (optional, *only used in first RATECRED.OfferWidget call*)
                //   url -- string -- the url of the object to like (default window.location.href)
                //   title -- string -- the title of the object to like (default document.title)
                //   keywords -- string -- specific keywords related to the content (optional)
    	    	//   width -- view width
	    		//   height -- view height
	    		//   view -- target view
        	
                var defaults = {
                				hostname: 'ratecred.com',
                                s: ['ratecred'],
                                url: window.location.href,
                                title: document.title,
                                css: 'http://media.ratecred.com.s3.amazonaws.com/prod/widget/offer/ratecred_offer_widget.css',
                                keywords: '',
                                width: 665,
                                height: 210,
                                city: 'San%20Francisco',
                                state: 'CA',
                                view: 'offer_widget'
                        },
                        i, len, wrapper, title, list, li, a, source,
                        css, hostname;            
                
                cfg = RATECRED.util.update(defaults, cfg);

                // Add CSS
                if (!RATECRED.OfferWidget._initialized) {
                        RATECRED.OfferWidget._initialized = true;
                        if (cfg.css) {
                                css = document.createElement('LINK');
                                css.rel = 'stylesheet';
                                css.type = 'text/css';
                                css.href = cfg.css;
                                (document.getElementsByTagName('HEAD')[0] || document.body).appendChild(css);
                        }
                }

                // Get current script object
                var script = document.getElementsByTagName('SCRIPT');
                script = script[script.length - 1];

                // Build Widget               
                //but only if this is the page we want
                if(cfg.url == window.location.href)
                {
                	
                	wrapper = document.createElement('DIV');
                    wrapper.className = 'ratecred_offer';
                    
                	
                    list = document.createElement('UL');
                    //list.className = 'ratecred_offer';
                    for (i=0, len=cfg.s.length; i<len; i++) {
                            if (source = RATECRED.Sources[cfg.s[i]]) {
                                    source = RATECRED.prepSource(cfg.s[i], source);
                                    li = document.createElement('LI');
                                    //li.className = 'ratecred_offer';
                                    li.height = cfg.height;
                                    RATECRED.util.update(li.style, {border: 'none', overflow: 'hidden', width: cfg.width+'px', height: cfg.height+'px', padding: '0px 0 0 0'});
                                    if (source.html) {
                                            a = source.html(cfg);
                                    } else {
                                            a = document.createElement('A');
                                            a.className = source.klass;
                                            a.href = '#';
                                            a.innerHTML = RATECRED.util.escape(source.name);
                                            if (source.title) a.title = source.title;
                                            if (source.like) a.onclick = source.like(cfg);
                                            if (source.basicLink) {
                                                    a.href = source.basicLink(a, cfg);
                                                    if (source.popup) {
                                                            a.onclick = function(url, target, attrs) {
                                                                    return function() {
                                                                            window.open(url, target, attrs);
                                                                            return false;
                                                                    };
                                                            }(a.href, source.popup.target, source.popup.attrs);
                                                    }
                                                    a.target = source.target;
                                            }
                                    }
                                    li.appendChild(a);
                                    list.appendChild(li);
                            }
                    }
                    wrapper.appendChild(list);
                    if (cfg.formkey) {
                        title = document.createElement('P');
                        title.innerHTML = "<a href='https://spreadsheets1.google.com/a/ratecred.com/viewform?formkey="+cfg.formkey+"'>Place your business local offer here!</a>";
                        wrapper.appendChild(title);
                    }
                }
                
                

                script.parentNode.insertBefore(wrapper, script);
                wrapper = title = list = li = script = source = null;
        };

        RATECRED.prepSource = function(name, source) {
                source = RATECRED.util.update({}, source);
                source.name = name;
                source.target = RATECRED.util.notundef(source.target, '_blank');
                source.klass = 'ratecred-' + RATECRED.util.escape(name);
                if (source.popup) {
                        if (typeof(source.popup) != 'object') source.popup = {};
                        source.popup.target = RATECRED.util.notundef(source.popup.target, '_blank');
                        source.popup.attrs = RATECRED.util.notundef(source.popup.attrs, 'width=360,height=360');
                }
                return source;
        };

        // Sources that can be used out-of-box (in alphabetical order)
        // The RATECRED.Sources object can be extended in a separate js file
        RATECRED.Sources = {
        		ratecred: {
			            html: function(cfg) {
			                    var elt = document.createElement('IFRAME');
			                    elt.width = cfg.width;
			                    elt.height = cfg.height;
			                    elt.src = 'http://'+cfg.hostname+'/rcadmin/do/offer/target?keywords='+encodeURIComponent(cfg.keywords)+'&view='+cfg.view+'&css='+encodeURIComponent(cfg.css)+'&address1='+encodeURIComponent(cfg.address1)+'&title='+encodeURIComponent(cfg.title)+'&teaser='+encodeURIComponent(cfg.teaser)+'&url='+encodeURIComponent(cfg.url)+ '&referrerId=' + cfg.referrerId;
			                    RATECRED.util.update(elt, {scrolling: 'no', frameBorder: '0', allowTransparency: 'true'});
			                    RATECRED.util.update(elt.style, {border: 'none', align:'left', overflow: 'hidden', width: cfg.width+'px', height: cfg.height+'px', marginheight:'0px', marginwidth: '0px'});
			                    return elt;
			            },
			            url: function(cfg) { return 'http://'+cfg.hostname;  },
			            basicLink: function(a, cfg) {
			                    var url = encodeURIComponent(cfg.url),
			                            title = encodeURIComponent(cfg.title);
			                    return 'http://'+cfg.hostname+'/view/sharer.html?u=' + url + '&t=' + title;
			            }
			    }       		
              };
}