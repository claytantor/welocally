// TODO - add full support for the "open graph" meta elements
// TODO - add more specifics to each service, such as buzz "imageurl"
if (!window.WELOCALLY) {
        window.WELOCALLY = {
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

if (!RATECRED.Widget) {
        RATECRED.Widget = function(placeId, reffererId, cfg) {
                // Params for cfg
                //   hostname - the name of the host to use 
                //   header -- the header text (or none) to give the widget (default 'Like this:')
                //   s -- array -- list of sites to share to (has a default)
                //   css -- string (or false) -- url for the css (optional, *only used in first RATECRED.Widget call*)
                //   url -- string -- the url of the object to like (default window.location.href)
                //   title -- string -- the title of the object to like (default document.title)
                //   type -- string -- the type of the object to like, e.g. product, activity, sport, bar, company (optional)
                var defaults = {
                				hostname: 'ratecred.com',
                                s: ['ratecred'],
                                url: window.location.href,
                                title: document.title,
                                css: 'http://ratecred.com/styles/ratecred_widget.css',
                                category: ''
                        },
                        i, len, wrapper, title, list, li, a, source,
                        css, hostname;            
                
                cfg = RATECRED.util.update(defaults, cfg);

                // Add CSS
                if (!RATECRED.Widget._initialized) {
                        RATECRED.Widget._initialized = true;
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
                wrapper = document.createElement('DIV');
                wrapper.className = 'ratecred';
                if (cfg.header) {
                        title = document.createElement('P');
                        title.innerHTML = RATECRED.util.escape(cfg.header);
                        wrapper.appendChild(title);
                }

                list = document.createElement('UL');
                for (i=0, len=cfg.s.length; i<len; i++) {
                        if (source = RATECRED.Sources[cfg.s[i]]) {
                                source = RATECRED.prepSource(cfg.s[i], source);
                                li = document.createElement('LI');
                                if (source.html) {
                                        a = source.html(placeId,reffererId,cfg);
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
			            html: function(placeId, reffererId, cfg) {
			                    // <iframe src="http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fdevelopers.facebook.com%2F&amp;layout=button_count&amp;show_faces=false&amp;width=25&amp;action=like&amp;colorscheme=light" scrolling="no" frameborder="0" allowTransparency="true" style="border:none; overflow:hidden; width:25px; height:px"></iframe>
			                    var elt = document.createElement('IFRAME'),
			                            width = 320,
			                            height = 95;
			                    elt.src = 'http://'+cfg.hostname+'/v2/place/' + placeId + '?url=' + encodeURIComponent(cfg.url) + '&refererId=' + reffererId + '&width=' + width + '&height=' + height + '&view=widget';
			                    RATECRED.util.update(elt, {scrolling: 'no', frameBorder: '0', allowTransparency: 'true'});
			                    RATECRED.util.update(elt.style, {border: 'none', overflow: 'hidden', width: width+'px', height: '300px', padding: '1px 0 0 0'});
			                    return elt;
			            },
			            url: 'http://ratecred.com',
			            basicLink: function(a, cfg) {
			                    var url = encodeURIComponent(cfg.url),
			                            title = encodeURIComponent(cfg.title);
			                    return 'http://'+cfg.hostname+'/view/sharer.html?u=' + url + '&t=' + title;
			            }
			    }       		
              };
}