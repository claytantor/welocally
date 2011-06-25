if (!window.WELOCALLY) {
    window.WELOCALLY = {};
}
if (!WELOCALLY.PublishWidget) {
    WELOCALLY.PublishWidget = function(cfg) {
        var error;
        // validate config
        if (!cfg) {
            error = "Please provide configuration for the WeLocally widget";
            cfg = {};
        }
        // url - the URL of the article
        if (!error && !cfg.url) {
            error = "Please provide the article URL for the WeLocally widget";
        }
        // only insert iframe if this is the page we want
        if (!error && cfg.url && cfg.url != window.location.href) {
            error = "Please embed the WeLocally widget on the page at " + cfg.url;
        }
        // title - the title of the article
        if (!error && !cfg.title) {
            error = "Please provide the article title for the WeLocally widget";
        }
        // publisher - the id of the publisher that is publishing the article
        if (!error && !cfg.publisher) {
            error = "Please provide the ID of the article publisher for the WeLocally widget";
        }
        // place - the id of the place that is the subject of the article
        if (!error && !cfg.place) {
            error = "Please provide the ID of a place for the WeLocally widget";
        }
        // summary (optional) - the summary of the article
        // hostname (optional) - the name of the host to use
        if (!cfg.hostname) {
            cfg.hostname = 'welocally.com';
        }

        // Get current script object
        var script = document.getElementsByTagName('SCRIPT');
        script = script[script.length - 1];

        // Build Widget
        var wrapper = document.createElement('DIV');
        wrapper.style.width = '100px';
        wrapper.style.height = '25px';
        wrapper.className = 'welocally_widget';

        if (error) {
            wrapper.style.color = 'red';
            wrapper.innerHTML = "WeLocally&nbsp;Widget&nbsp;(mouseover)";
            wrapper.title = error;
            wrapper.style.cursor = "pointer";
        } else {
            var iframe = document.createElement('IFRAME');
            iframe.src = 'http://' + cfg.hostname + '/rcadmin/widget/publish?publisher=' + encodeURIComponent(cfg.publisher)
                    + '&place=' + encodeURIComponent(cfg.place)
                    + '&name=' + encodeURIComponent(cfg.title)
                    + '&summary=' + encodeURIComponent(cfg.summary)
                    + '&url=' + encodeURIComponent(cfg.url);
            iframe.width = '100%';
            iframe.height = '100%';
            iframe.scrolling = 'no';
            iframe.frameBorder = '0';
            iframe.allowTransparency = 'true';
            iframe.style.border = 'none';
            iframe.style.overflow = 'hidden';
            iframe.style.marginheight = '0px';
            iframe.style.marginwidth = '0px';
            wrapper.appendChild(iframe);
            iframe = null;
        }
        script.parentNode.insertBefore(wrapper, script);
        script = wrapper = null;
    };
}