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
                        },
                        guidGenerator: function() {
                    	    return (WELOCALLY.util.S4()+WELOCALLY.util.S4()+"-"+
                    	    		WELOCALLY.util.S4()+"-"+WELOCALLY.util.S4()+"-"+
                    	    		WELOCALLY.util.S4()+"-"+
                    	    		WELOCALLY.util.S4()+WELOCALLY.util.S4()+WELOCALLY.util.S4());
                        },
                        keyGenerator: function() {
                    	    return (WELOCALLY.util.S4()+WELOCALLY.util.S4());
                        },
                        tokenGenerator: function() {
                        	 return (WELOCALLY.util.S4()+WELOCALLY.util.S4()+
                     	    		WELOCALLY.util.S4()+WELOCALLY.util.S4()+
                     	    		WELOCALLY.util.S4()+
                     	    		WELOCALLY.util.S4()+WELOCALLY.util.S4()+WELOCALLY.util.S4());
                        },
                        S4: function() {
                 	       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                	    }
                }
        };
}

