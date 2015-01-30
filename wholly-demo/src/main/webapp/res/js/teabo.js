/**
 * "teabo" Selector Engine Copyright (c) 2009 James Padolsey
 * ------------------------------------------------------- Dual licensed under
 * the MIT and GPL licenses. -
 * http://www.opensource.org/licenses/mit-license.php -
 * http://www.gnu.org/copyleft/gpl.html
 * ------------------------------------------------------- Version: 0.01 (BETA)
 */
(function( window, undefined ) {
	// Use the correct document accordingly with window argument (sandbox)
var document = window.document,
		navigator = window.navigator,
		location = window.location;
var teabo=(function() {
		var teabo=function( selector, context ){
			return new teabo.fn.init(selector, context, rootTeabo);
		},
		_teabo = window.teabo,
		_$ = window.$,
		rootTeabo,
		// A simple way to check for HTML strings or ID strings
		// Prioritize #id over <tag> to avoid XSS via location.hash (#9521)
		quickExpr = /^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/,

		// Check if a string has a non-whitespace character in it
		rnotwhite = /\S/,

		// Used for trimming whitespace
		trimLeft = /^\s+/,
		trimRight = /\s+$/,

		// Match a standalone tag
		rsingleTag = /^<(\w+)\s*\/?>(?:<\/\1>)?$/,

		// JSON RegExp
		rvalidchars = /^[\],:{}\s]*$/,
		rvalidescape = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,
		rvalidtokens = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
		rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g,

		// Useragent RegExp
		rwebkit = /(webkit)[ \/]([\w.]+)/,
		ropera = /(opera)(?:.*version)?[ \/]([\w.]+)/,
		rmsie = /(msie) ([\w.]+)/,
		rmozilla = /(mozilla)(?:.*? rv:([\w.]+))?/,

		// Matches dashed string for camelizing
		rdashAlpha = /-([a-z]|[0-9])/ig,
		rmsPrefix = /^-ms-/,

		// Used by teabo.camelCase as callback to replace()
		fcamelCase = function( all, letter ) {
			return ( letter + "" ).toUpperCase();
		},
		
		// Keep a UserAgent string for use with teabo.browser
		userAgent = navigator.userAgent,

		// For matching the engine and version of the browser
		browserMatch,
		
		readyList,
		
		// The ready event handler
		DOMContentLoaded,
		// Save a reference to some core methods
		toString = Object.prototype.toString,
		hasOwn = Object.prototype.hasOwnProperty,
		push = Array.prototype.push,
		slice = Array.prototype.slice,
		trim = String.prototype.trim,
		indexOf = Array.prototype.indexOf,
		// [[Class]] -> type pairs
		class2type = {};

		teabo.fn=teabo.prototype={
				constructor:teabo,
				init:function( selector, context, rootTeabo ){
					var match, elem, ret, doc;
					
					// Handle $(""), $(null), or $(undefined)
					if ( !selector ) {
						return this;
					}
					// Handle $(DOMElement)
					if ( selector.nodeType ) {
						this.context = this[0] = selector;
						this.length = 1;
						return this;
					}

					// The body element only exists once, optimize finding it
					if ( selector === "body" && !context && document.body ) {
						this.context = document;
						this[0] = document.body;
						this.selector = selector;
						this.length = 1;
						return this;
					}

					// Handle HTML strings
					if ( teabo.isFunction( selector ) ) {
						return rootTeabo.ready( selector );
					} else if ( typeof selector === "string" ) {
						// Are we dealing with HTML string or an ID?
						if ( selector.charAt(0) === "<" && selector.charAt( selector.length - 1 ) === ">" && selector.length >= 3 ) {
							// Assume that strings that start and end with <> are HTML and skip the regex check
							match = [ null, selector, null ];

						} else {
							match = quickExpr.exec( selector );
						}

						// Verify a match, and that no context was specified for #id
						if ( match && (match[1] || !context) ) {
							// HANDLE: $(html) -> $(array)
							if ( match[1] ) {
								context = context instanceof teabo ? context[0] : context;
								doc = ( context ? context.ownerDocument || context : document );

								// If a single string is passed in and it's a single tag
								// just do a createElement and skip the rest
								ret = rsingleTag.exec( selector );

								if ( ret ) {
									if ( teabo.isPlainObject( context ) ) {
										selector = [ document.createElement( ret[1] ) ];
										teabo.fn.attr.call( selector, context, true );

									} else {
										selector = [ doc.createElement( ret[1] ) ];
									}

								} else {
									ret = teabo.buildFragment( [ match[1] ], [ doc ] );
									selector = ( ret.cacheable ? teabo.clone(ret.fragment) : ret.fragment ).childNodes;
								}

								return teabo.merge( this, selector );

							// HANDLE: $("#id")
							} else {
								elem = document.getElementById( match[2] );

								// Check parentNode to catch when Blackberry 4.6 returns
								// nodes that are no longer in the document #6963
								if ( elem && elem.parentNode ) {
									// Handle the case where IE and Opera return items
									// by name instead of ID
									if ( elem.id !== match[2] ) {
										return rootTeabo.find( selector );
									}

									// Otherwise, we inject the element directly into the teabo object
									this.length = 1;
									this[0] = elem;
								}

								this.context = document;
								this.selector = selector;
								return this;
							}

						// HANDLE: $(expr, $(...))
						} else if ( !context || context.teabo ) {
							return ( context || rootTeabo ).find( selector );
							
						// HANDLE: $(expr, context)
						// (which is just equivalent to: $(context).find(expr)
						} else {
							return this.constructor( context ).find( selector );
						}
					} 
					if ( selector.selector !== undefined ) {
						this.selector = selector.selector;
						this.context = selector.context;
					}
					return teabo.makeArray( selector, this );
				},
				// Start with an empty selector
				selector: "",

				// The current version of teabo being used
				teabo: "1.7.2",

				// The default length of a teabo object is 0
				length: 0,

				// The number of elements contained in the matched element set
				size: function() {
					return this.length;
				},

				toArray: function() {
					return slice.call( this, 0 );
				},

				// Get the Nth element in the matched element set OR
				// Get the whole matched element set as a clean array
				get: function( num ) {
					return num == null ?

						// Return a 'clean' array
						this.toArray() :

						// Return just the object
						( num < 0 ? this[ this.length + num ] : this[ num ] );
				},

				// Take an array of elements and push it onto the stack
				// (returning the new matched element set)
				pushStack: function( elems, name, selector ) {
					// Build a new teabo matched element set
					var ret = this.constructor();

					if ( teabo.isArray( elems ) ) {
						push.apply( ret, elems );

					} else {
						teabo.merge( ret, elems );
					}

					// Add the old object onto the stack (as a reference)
					ret.prevObject = this;

					ret.context = this.context;

					if ( name === "find" ) {
						ret.selector = this.selector + ( this.selector ? " " : "" ) + selector;
					} else if ( name ) {
						ret.selector = this.selector + "." + name + "(" + selector + ")";
					}

					// Return the newly-formed element set
					return ret;
				},

				// Execute a callback for every element in the matched set.
				// (You can seed the arguments with an array of args, but this is
				// only used internally.)
				each: function( callback, args ) {
					return teabo.each( this, callback, args );
				},

				ready: function( fn ) {
					// Attach the listeners
					teabo.bindReady();

					// Add the callback
					readyList.add( fn );

					return this;
				},

				eq: function( i ) {
					i = +i;
					return i === -1 ?
						this.slice( i ) :
						this.slice( i, i + 1 );
				},

				first: function() {
					return this.eq( 0 );
				},

				last: function() {
					return this.eq( -1 );
				},

				slice: function() {
					return this.pushStack( slice.apply( this, arguments ),
						"slice", slice.call(arguments).join(",") );
				},

				map: function( callback ) {
					return this.pushStack( teabo.map(this, function( elem, i ) {
						return callback.call( elem, i, elem );
					}));
				},

				end: function() {
					return this.prevObject || this.constructor(null);
				},

				// For internal use only.
				// Behaves like an Array's method, not like a teabo method.
				push: push,
				sort: [].sort,
				splice: [].splice
		};
		
		teabo.fn.init.prototype = teabo.fn;
		
		teabo.extend = teabo.fn.extend = function() {
			var options, name, src, copy, copyIsArray, clone,
				target = arguments[0] || {},
				i = 1,
				length = arguments.length,
				deep = false;

			// Handle a deep copy situation
			if ( typeof target === "boolean" ) {
				deep = target;
				target = arguments[1] || {};
				// skip the boolean and the target
				i = 2;
			}

			// Handle case when target is a string or something (possible in deep copy)
			if ( typeof target !== "object" && !teabo.isFunction(target) ) {
				target = {};
			}

			// extend teabo itself if only one argument is passed
			if ( length === i ) {
				target = this;
				--i;
			}

			for ( ; i < length; i++ ) {
				// Only deal with non-null/undefined values
				if ( (options = arguments[ i ]) != null ) {
					// Extend the base object
					for ( name in options ) {
						src = target[ name ];
						copy = options[ name ];

						// Prevent never-ending loop
						if ( target === copy ) {
							continue;
						}

						// Recurse if we're merging plain objects or arrays
						if ( deep && copy && ( teabo.isPlainObject(copy) || (copyIsArray = teabo.isArray(copy)) ) ) {
							if ( copyIsArray ) {
								copyIsArray = false;
								clone = src && teabo.isArray(src) ? src : [];

							} else {
								clone = src && teabo.isPlainObject(src) ? src : {};
							}

							// Never move original objects, clone them
							target[ name ] = teabo.extend( deep, clone, copy );

						// Don't bring in undefined values
						} else if ( copy !== undefined ) {
							target[ name ] = copy;
						}
					}
				}
			}

			// Return the modified object
			return target;
		};
		
		teabo.extend({
				noConflict: function( deep ) {
					if ( window.$ === teabo ) {
						window.$ = _$;
					}

					if ( deep && window.teabo === teabo ) {
						window.teabo = _teabo;
					}

					return teabo;
				},

				// Is the DOM ready to be used? Set to true once it occurs.
				isReady: false,

				// A counter to track how many items to wait for before
				// the ready event fires. See #6781
				readyWait: 1,

				// Hold (or release) the ready event
				holdReady: function( hold ) {
					if ( hold ) {
						teabo.readyWait++;
					} else {
						teabo.ready( true );
					}
				},

				// Handle when the DOM is ready
				ready: function( wait ) {
					// Either a released hold or an DOMready/load event and not yet ready
					if ( (wait === true && !--teabo.readyWait) || (wait !== true && !teabo.isReady) ) {
						// Make sure body exists, at least, in case IE gets a little overzealous (ticket #5443).
						if ( !document.body ) {
							return setTimeout( teabo.ready, 1 );
						}

						// Remember that the DOM is ready
						teabo.isReady = true;

						// If a normal DOM Ready event fired, decrement, and wait if need be
						if ( wait !== true && --teabo.readyWait > 0 ) {
							return;
						}

						// If there are functions bound, to execute
						readyList.fireWith( document, [ teabo ] );

						// Trigger any bound ready events
						if ( teabo.fn.trigger ) {
							teabo( document ).trigger( "ready" ).off( "ready" );
						}
					}
				},

				bindReady: function() {
					if ( readyList ) {
						return;
					}

					readyList = teabo.Callbacks( "once memory" );

					// Catch cases where $(document).ready() is called after the
					// browser event has already occurred.
					if ( document.readyState === "complete" ) {
						// Handle it asynchronously to allow scripts the opportunity to delay ready
						return setTimeout( teabo.ready, 1 );
					}

					// Mozilla, Opera and webkit nightlies currently support this event
					if ( document.addEventListener ) {
						// Use the handy event callback
						document.addEventListener( "DOMContentLoaded", DOMContentLoaded, false );

						// A fallback to window.onload, that will always work
						window.addEventListener( "load", teabo.ready, false );

					// If IE event model is used
					} else if ( document.attachEvent ) {
						// ensure firing before onload,
						// maybe late but safe also for iframes
						document.attachEvent( "onreadystatechange", DOMContentLoaded );

						// A fallback to window.onload, that will always work
						window.attachEvent( "onload", teabo.ready );

						// If IE and not a frame
						// continually check to see if the document is ready
						var toplevel = false;

						try {
							toplevel = window.frameElement == null;
						} catch(e) {}

						if ( document.documentElement.doScroll && toplevel ) {
							doScrollCheck();
						}
					}
				},

				// See test/unit/core.js for details concerning isFunction.
				// Since version 1.3, DOM methods and functions like alert
				// aren't supported. They return false on IE (#2968).
				isFunction: function( obj ) {
					return teabo.type(obj) === "function";
				},

				isArray: Array.isArray || function( obj ) {
					return teabo.type(obj) === "array";
				},

				isWindow: function( obj ) {
					return obj != null && obj == obj.window;
				},

				isNumeric: function( obj ) {
					return !isNaN( parseFloat(obj) ) && isFinite( obj );
				},

				type: function( obj ) {
					return obj == null ?
						String( obj ) :
						class2type[ toString.call(obj) ] || "object";
				},

				isPlainObject: function( obj ) {
					// Must be an Object.
					// Because of IE, we also have to check the presence of the constructor property.
					// Make sure that DOM nodes and window objects don't pass through, as well
					if ( !obj || teabo.type(obj) !== "object" || obj.nodeType || teabo.isWindow( obj ) ) {
						return false;
					}

					try {
						// Not own constructor property must be Object
						if ( obj.constructor &&
							!hasOwn.call(obj, "constructor") &&
							!hasOwn.call(obj.constructor.prototype, "isPrototypeOf") ) {
							return false;
						}
					} catch ( e ) {
						// IE8,9 Will throw exceptions on certain host objects #9897
						return false;
					}

					// Own properties are enumerated firstly, so to speed up,
					// if last one is own, then all properties are own.

					var key;
					for ( key in obj ) {}

					return key === undefined || hasOwn.call( obj, key );
				},

				isEmptyObject: function( obj ) {
					for ( var name in obj ) {
						return false;
					}
					return true;
				},

				error: function( msg ) {
					throw new Error( msg );
				},

				parseJSON: function( data ) {
					if ( typeof data !== "string" || !data ) {
						return null;
					}

					// Make sure leading/trailing whitespace is removed (IE can't handle it)
					data = teabo.trim( data );

					// Attempt to parse using the native JSON parser first
					if ( window.JSON && window.JSON.parse ) {
						return window.JSON.parse( data );
					}

					// Make sure the incoming data is actual JSON
					// Logic borrowed from http://json.org/json2.js
					if ( rvalidchars.test( data.replace( rvalidescape, "@" )
						.replace( rvalidtokens, "]" )
						.replace( rvalidbraces, "")) ) {

						return ( new Function( "return " + data ) )();

					}
					teabo.error( "Invalid JSON: " + data );
				},

				// Cross-browser xml parsing
				parseXML: function( data ) {
					if ( typeof data !== "string" || !data ) {
						return null;
					}
					var xml, tmp;
					try {
						if ( window.DOMParser ) { // Standard
							tmp = new DOMParser();
							xml = tmp.parseFromString( data , "text/xml" );
						} else { // IE
							xml = new ActiveXObject( "Microsoft.XMLDOM" );
							xml.async = "false";
							xml.loadXML( data );
						}
					} catch( e ) {
						xml = undefined;
					}
					if ( !xml || !xml.documentElement || xml.getElementsByTagName( "parsererror" ).length ) {
						teabo.error( "Invalid XML: " + data );
					}
					return xml;
				},

				noop: function() {},

				// Evaluates a script in a global context
				// Workarounds based on findings by Jim Driscoll
				// http://weblogs.java.net/blog/driscoll/archive/2009/09/08/eval-javascript-global-context
				globalEval: function( data ) {
					if ( data && rnotwhite.test( data ) ) {
						// We use execScript on Internet Explorer
						// We use an anonymous function so that context is window
						// rather than teabo in Firefox
						( window.execScript || function( data ) {
							window[ "eval" ].call( window, data );
						} )( data );
					}
				},

				// Convert dashed to camelCase; used by the css and data modules
				// Microsoft forgot to hump their vendor prefix (#9572)
				camelCase: function( string ) {
					return string.replace( rmsPrefix, "ms-" ).replace( rdashAlpha, fcamelCase );
				},

				nodeName: function( elem, name ) {
					return elem.nodeName && elem.nodeName.toUpperCase() === name.toUpperCase();
				},

				// args is for internal usage only
				each: function( object, callback, args ) {
					var name, i = 0,
						length = object.length,
						isObj = length === undefined || teabo.isFunction( object );

					if ( args ) {
						if ( isObj ) {
							for ( name in object ) {
								if ( callback.apply( object[ name ], args ) === false ) {
									break;
								}
							}
						} else {
							for ( ; i < length; ) {
								if ( callback.apply( object[ i++ ], args ) === false ) {
									break;
								}
							}
						}

					// A special, fast, case for the most common use of each
					} else {
						if ( isObj ) {
							for ( name in object ) {
								if ( callback.call( object[ name ], name, object[ name ] ) === false ) {
									break;
								}
							}
						} else {
							for ( ; i < length; ) {
								if ( callback.call( object[ i ], i, object[ i++ ] ) === false ) {
									break;
								}
							}
						}
					}

					return object;
				},

				// Use native String.trim function wherever possible
				trim: trim ?
					function( text ) {
						return text == null ?
							"" :
							trim.call( text );
					} :

					// Otherwise use our own trimming functionality
					function( text ) {
						return text == null ?
							"" :
							text.toString().replace( trimLeft, "" ).replace( trimRight, "" );
					},

				// results is for internal usage only
				makeArray: function( array, results ) {
					var ret = results || [];

					if ( array != null ) {
						// The window, strings (and functions) also have 'length'
						// Tweaked logic slightly to handle Blackberry 4.7 RegExp issues #6930
						var type = teabo.type( array );
						
						if ( array.length == null || type === "string" || type === "function" || type === "regexp" || teabo.isWindow( array ) ) {
							push.call( ret, array );
						} else {
							teabo.merge( ret, array );
						}
					}

					return ret;
				},

				inArray: function( elem, array, i ) {
					var len;

					if ( array ) {
						if ( indexOf ) {
							return indexOf.call( array, elem, i );
						}

						len = array.length;
						i = i ? i < 0 ? Math.max( 0, len + i ) : i : 0;

						for ( ; i < len; i++ ) {
							// Skip accessing in sparse arrays
							if ( i in array && array[ i ] === elem ) {
								return i;
							}
						}
					}

					return -1;
				},

				merge: function( first, second ) {
					var i = first.length,
						j = 0;

					if ( typeof second.length === "number" ) {
						for ( var l = second.length; j < l; j++ ) {
							first[ i++ ] = second[ j ];
						}

					} else {
						while ( second[j] !== undefined ) {
							first[ i++ ] = second[ j++ ];
						}
					}

					first.length = i;

					return first;
				},

				grep: function( elems, callback, inv ) {
					var ret = [], retVal;
					inv = !!inv;

					// Go through the array, only saving the items
					// that pass the validator function
					for ( var i = 0, length = elems.length; i < length; i++ ) {
						retVal = !!callback( elems[ i ], i );
						if ( inv !== retVal ) {
							ret.push( elems[ i ] );
						}
					}

					return ret;
				},

				// arg is for internal usage only
				map: function( elems, callback, arg ) {
					var value, key, ret = [],
						i = 0,
						length = elems.length,
						// teabo objects are treated as arrays
						isArray = elems instanceof teabo || length !== undefined && typeof length === "number" && ( ( length > 0 && elems[ 0 ] && elems[ length -1 ] ) || length === 0 || teabo.isArray( elems ) ) ;

					// Go through the array, translating each of the items to their
					if ( isArray ) {
						for ( ; i < length; i++ ) {
							value = callback( elems[ i ], i, arg );

							if ( value != null ) {
								ret[ ret.length ] = value;
							}
						}

					// Go through every key on the object,
					} else {
						for ( key in elems ) {
							value = callback( elems[ key ], key, arg );

							if ( value != null ) {
								ret[ ret.length ] = value;
							}
						}
					}

					// Flatten any nested arrays
					return ret.concat.apply( [], ret );
				},

				// A global GUID counter for objects
				guid: 1,

				// Bind a function to a context, optionally partially applying any
				// arguments.
				proxy: function( fn, context ) {
					if ( typeof context === "string" ) {
						var tmp = fn[ context ];
						context = fn;
						fn = tmp;
					}

					// Quick check to determine if target is callable, in the spec
					// this throws a TypeError, but we will just return undefined.
					if ( !teabo.isFunction( fn ) ) {
						return undefined;
					}

					// Simulated bind
					var args = slice.call( arguments, 2 ),
						proxy = function() {
							return fn.apply( context, args.concat( slice.call( arguments ) ) );
						};

					// Set the guid of unique handler to the same of original handler, so it can be removed
					proxy.guid = fn.guid = fn.guid || proxy.guid || teabo.guid++;

					return proxy;
				},

				// Mutifunctional method to get and set values to a collection
				// The value/s can optionally be executed if it's a function
				access: function( elems, fn, key, value, chainable, emptyGet, pass ) {
					var exec,
						bulk = key == null,
						i = 0,
						length = elems.length;

					// Sets many values
					if ( key && typeof key === "object" ) {
						for ( i in key ) {
							teabo.access( elems, fn, i, key[i], 1, emptyGet, value );
						}
						chainable = 1;

					// Sets one value
					} else if ( value !== undefined ) {
						// Optionally, function values get executed if exec is true
						exec = pass === undefined && teabo.isFunction( value );

						if ( bulk ) {
							// Bulk operations only iterate when executing function values
							if ( exec ) {
								exec = fn;
								fn = function( elem, key, value ) {
									return exec.call( teabo( elem ), value );
								};

							// Otherwise they run against the entire set
							} else {
								fn.call( elems, value );
								fn = null;
							}
						}

						if ( fn ) {
							for (; i < length; i++ ) {
								fn( elems[i], key, exec ? value.call( elems[i], i, fn( elems[i], key ) ) : value, pass );
							}
						}

						chainable = 1;
					}

					return chainable ?
						elems :

						// Gets
						bulk ?
							fn.call( elems ) :
							length ? fn( elems[0], key ) : emptyGet;
				},

				now: function() {
					return ( new Date() ).getTime();
				},

				// Use of teabo.browser is frowned upon.
				// More details: http://docs.teabo.com/Utilities/teabo.browser
				uaMatch: function( ua ) {
					ua = ua.toLowerCase();

					var match = rwebkit.exec( ua ) ||
						ropera.exec( ua ) ||
						rmsie.exec( ua ) ||
						ua.indexOf("compatible") < 0 && rmozilla.exec( ua ) ||
						[];

					return { browser: match[1] || "", version: match[2] || "0" };
				},

				sub: function() {
					function teaboSub( selector, context ) {
						return new teaboSub.fn.init( selector, context );
					}
					teabo.extend( true, teaboSub, this );
					teaboSub.superclass = this;
					teaboSub.fn = teaboSub.prototype = this();
					teaboSub.fn.constructor = teaboSub;
					teaboSub.sub = this.sub;
					teaboSub.fn.init = function init( selector, context ) {
						if ( context && context instanceof teabo && !(context instanceof teaboSub) ) {
							context = teaboSub( context );
						}

						return teabo.fn.init.call( this, selector, context, rootTeaboSub );
					};
					teaboSub.fn.init.prototype = teaboSub.fn;
					var rootTeaboSub = teaboSub(document);
					return teaboSub;
				},

				browser: {}
			});
			
		teabo.each("Boolean Number String Function Array Date RegExp Object".split(" "), function(i, name) {
			class2type[ "[object " + name + "]" ] = name.toLowerCase();
		});
		rootTeabo = teabo(document);
		// Cleanup functions for the document ready method
		if ( document.addEventListener ) {
			DOMContentLoaded = function() {
				document.removeEventListener( "DOMContentLoaded", DOMContentLoaded, false );
				teabo.ready();
			};

		} else if ( document.attachEvent ) {
			DOMContentLoaded = function() {
				// Make sure body exists, at least, in case IE gets a little overzealous (ticket #5443).
				if ( document.readyState === "complete" ) {
					document.detachEvent( "onreadystatechange", DOMContentLoaded );
					teabo.ready();
				}
			};
		}
		
		// The DOM ready check for Internet Explorer
		function doScrollCheck() {
			if ( teabo.isReady ) {
				return;
			}

			try {
				// If IE is used, use the trick by Diego Perini
				// http://javascript.nwbox.com/IEContentLoaded/
				document.documentElement.doScroll("left");
			} catch(e) {
				setTimeout( doScrollCheck, 1 );
				return;
			}

			// and execute any waiting functions
			teabo.ready();
		}
		
		return teabo;
	})();

	// String to Object flags format cache
	var flagsCache = {};

	// Convert String-formatted flags into Object-formatted ones and store in cache
	function createFlags( flags ) {
		var object = flagsCache[ flags ] = {},
			i, length;
		flags = flags.split( /\s+/ );
		for ( i = 0, length = flags.length; i < length; i++ ) {
			object[ flags[i] ] = true;
		}
		return object;
	}
	teabo.Callbacks = function( flags ) {

		// Convert flags from String-formatted to Object-formatted
		// (we check in cache first)
		flags = flags ? ( flagsCache[ flags ] || createFlags( flags ) ) : {};

		var // Actual callback list
			list = [],
			// Stack of fire calls for repeatable lists
			stack = [],
			// Last fire value (for non-forgettable lists)
			memory,
			// Flag to know if list was already fired
			fired,
			// Flag to know if list is currently firing
			firing,
			// First callback to fire (used internally by add and fireWith)
			firingStart,
			// End of the loop when firing
			firingLength,
			// Index of currently firing callback (modified by remove if needed)
			firingIndex,
			// Add one or several callbacks to the list
			add = function( args ) {
				var i,
					length,
					elem,
					type,
					actual;
				for ( i = 0, length = args.length; i < length; i++ ) {
					elem = args[ i ];
					type = teabo.type( elem );
					if ( type === "array" ) {
						// Inspect recursively
						add( elem );
					} else if ( type === "function" ) {
						// Add if not in unique mode and callback is not in
						if ( !flags.unique || !self.has( elem ) ) {
							list.push( elem );
						}
					}
				}
			},
			// Fire callbacks
			fire = function( context, args ) {
				args = args || [];
				memory = !flags.memory || [ context, args ];
				fired = true;
				firing = true;
				firingIndex = firingStart || 0;
				firingStart = 0;
				firingLength = list.length;
				for ( ; list && firingIndex < firingLength; firingIndex++ ) {
					if ( list[ firingIndex ].apply( context, args ) === false && flags.stopOnFalse ) {
						memory = true; // Mark as halted
						break;
					}
				}
				firing = false;
				if ( list ) {
					if ( !flags.once ) {
						if ( stack && stack.length ) {
							memory = stack.shift();
							self.fireWith( memory[ 0 ], memory[ 1 ] );
						}
					} else if ( memory === true ) {
						self.disable();
					} else {
						list = [];
					}
				}
			},
			// Actual Callbacks object
			self = {
				// Add a callback or a collection of callbacks to the list
				add: function() {
					if ( list ) {
						var length = list.length;
						add( arguments );
						// Do we need to add the callbacks to the
						// current firing batch?
						if ( firing ) {
							firingLength = list.length;
						// With memory, if we're not firing then
						// we should call right away, unless previous
						// firing was halted (stopOnFalse)
						} else if ( memory && memory !== true ) {
							firingStart = length;
							fire( memory[ 0 ], memory[ 1 ] );
						}
					}
					return this;
				},
				// Remove a callback from the list
				remove: function() {
					if ( list ) {
						var args = arguments,
							argIndex = 0,
							argLength = args.length;
						for ( ; argIndex < argLength ; argIndex++ ) {
							for ( var i = 0; i < list.length; i++ ) {
								if ( args[ argIndex ] === list[ i ] ) {
									// Handle firingIndex and firingLength
									if ( firing ) {
										if ( i <= firingLength ) {
											firingLength--;
											if ( i <= firingIndex ) {
												firingIndex--;
											}
										}
									}
									// Remove the element
									list.splice( i--, 1 );
									// If we have some unicity property then
									// we only need to do this once
									if ( flags.unique ) {
										break;
									}
								}
							}
						}
					}
					return this;
				},
				// Control if a given callback is in the list
				has: function( fn ) {
					if ( list ) {
						var i = 0,
							length = list.length;
						for ( ; i < length; i++ ) {
							if ( fn === list[ i ] ) {
								return true;
							}
						}
					}
					return false;
				},
				// Remove all callbacks from the list
				empty: function() {
					list = [];
					return this;
				},
				// Have the list do nothing anymore
				disable: function() {
					list = stack = memory = undefined;
					return this;
				},
				// Is it disabled?
				disabled: function() {
					return !list;
				},
				// Lock the list in its current state
				lock: function() {
					stack = undefined;
					if ( !memory || memory === true ) {
						self.disable();
					}
					return this;
				},
				// Is it locked?
				locked: function() {
					return !stack;
				},
				// Call all callbacks with the given context and arguments
				fireWith: function( context, args ) {
					if ( stack ) {
						if ( firing ) {
							if ( !flags.once ) {
								stack.push( [ context, args ] );
							}
						} else if ( !( flags.once && memory ) ) {
							fire( context, args );
						}
					}
					return this;
				},
				// Call all the callbacks with the given arguments
				fire: function() {
					self.fireWith( this, arguments );
					return this;
				},
				// To know if the callbacks have already been called at least once
				fired: function() {
					return !!fired;
				}
			};

		return self;
	};

	var // Static reference to slice
		sliceDeferred = [].slice;

	teabo.extend({

		Deferred: function( func ) {
			var doneList = teabo.Callbacks( "once memory" ),
				failList = teabo.Callbacks( "once memory" ),
				progressList = teabo.Callbacks( "memory" ),
				state = "pending",
				lists = {
					resolve: doneList,
					reject: failList,
					notify: progressList
				},
				promise = {
					done: doneList.add,
					fail: failList.add,
					progress: progressList.add,

					state: function() {
						return state;
					},

					// Deprecated
					isResolved: doneList.fired,
					isRejected: failList.fired,

					then: function( doneCallbacks, failCallbacks, progressCallbacks ) {
						deferred.done( doneCallbacks ).fail( failCallbacks ).progress( progressCallbacks );
						return this;
					},
					always: function() {
						deferred.done.apply( deferred, arguments ).fail.apply( deferred, arguments );
						return this;
					},
					pipe: function( fnDone, fnFail, fnProgress ) {
						return teabo.Deferred(function( newDefer ) {
							teabo.each( {
								done: [ fnDone, "resolve" ],
								fail: [ fnFail, "reject" ],
								progress: [ fnProgress, "notify" ]
							}, function( handler, data ) {
								var fn = data[ 0 ],
									action = data[ 1 ],
									returned;
								if ( teabo.isFunction( fn ) ) {
									deferred[ handler ](function() {
										returned = fn.apply( this, arguments );
										if ( returned && teabo.isFunction( returned.promise ) ) {
											returned.promise().then( newDefer.resolve, newDefer.reject, newDefer.notify );
										} else {
											newDefer[ action + "With" ]( this === deferred ? newDefer : this, [ returned ] );
										}
									});
								} else {
									deferred[ handler ]( newDefer[ action ] );
								}
							});
						}).promise();
					},
					// Get a promise for this deferred
					// If obj is provided, the promise aspect is added to the object
					promise: function( obj ) {
						if ( obj == null ) {
							obj = promise;
						} else {
							for ( var key in promise ) {
								obj[ key ] = promise[ key ];
							}
						}
						return obj;
					}
				},
				deferred = promise.promise({}),
				key;

			for ( key in lists ) {
				deferred[ key ] = lists[ key ].fire;
				deferred[ key + "With" ] = lists[ key ].fireWith;
			}

			// Handle state
			deferred.done( function() {
				state = "resolved";
			}, failList.disable, progressList.lock ).fail( function() {
				state = "rejected";
			}, doneList.disable, progressList.lock );

			// Call given func if any
			if ( func ) {
				func.call( deferred, deferred );
			}

			// All done!
			return deferred;
		},

		// Deferred helper
		when: function( firstParam ) {
			var args = sliceDeferred.call( arguments, 0 ),
				i = 0,
				length = args.length,
				pValues = new Array( length ),
				count = length,
				pCount = length,
				deferred = length <= 1 && firstParam && teabo.isFunction( firstParam.promise ) ?
					firstParam :
					teabo.Deferred(),
				promise = deferred.promise();
			function resolveFunc( i ) {
				return function( value ) {
					args[ i ] = arguments.length > 1 ? sliceDeferred.call( arguments, 0 ) : value;
					if ( !( --count ) ) {
						deferred.resolveWith( deferred, args );
					}
				};
			}
			function progressFunc( i ) {
				return function( value ) {
					pValues[ i ] = arguments.length > 1 ? sliceDeferred.call( arguments, 0 ) : value;
					deferred.notifyWith( promise, pValues );
				};
			}
			if ( length > 1 ) {
				for ( ; i < length; i++ ) {
					if ( args[ i ] && args[ i ].promise && teabo.isFunction( args[ i ].promise ) ) {
						args[ i ].promise().then( resolveFunc(i), deferred.reject, progressFunc(i) );
					} else {
						--count;
					}
				}
				if ( !count ) {
					deferred.resolveWith( deferred, args );
				}
			} else if ( deferred !== firstParam ) {
				deferred.resolveWith( deferred, length ? [ firstParam ] : [] );
			}
			return promise;
		}
	});	
	
teabo.support = (function() {

	var support,
		all,
		a,
		select,
		opt,
		input,
		fragment,
		tds,
		events,
		eventName,
		i,
		isSupported,
		div = document.createElement( "div" ),
		documentElement = document.documentElement;

	// Preliminary tests
	div.setAttribute("className", "t");
	div.innerHTML = "   <link/><table></table><a href='/a' style='top:1px;float:left;opacity:.55;'>a</a><input type='checkbox'/>";

	all = div.getElementsByTagName( "*" );
	a = div.getElementsByTagName( "a" )[ 0 ];

	// Can't get basic test support
	if ( !all || !all.length || !a ) {
		return {};
	}

	// First batch of supports tests
	select = document.createElement( "select" );
	opt = select.appendChild( document.createElement("option") );
	input = div.getElementsByTagName( "input" )[ 0 ];

	support = {
		// IE strips leading whitespace when .innerHTML is used
		leadingWhitespace: ( div.firstChild.nodeType === 3 ),

		// Make sure that tbody elements aren't automatically inserted
		// IE will insert them into empty tables
		tbody: !div.getElementsByTagName("tbody").length,

		// Make sure that link elements get serialized correctly by innerHTML
		// This requires a wrapper element in IE
		htmlSerialize: !!div.getElementsByTagName("link").length,

		// Get the style information from getAttribute
		// (IE uses .cssText instead)
		style: /top/.test( a.getAttribute("style") ),

		// Make sure that URLs aren't manipulated
		// (IE normalizes it by default)
		hrefNormalized: ( a.getAttribute("href") === "/a" ),

		// Make sure that element opacity exists
		// (IE uses filter instead)
		// Use a regex to work around a WebKit issue. See #5145
		opacity: /^0.55/.test( a.style.opacity ),

		// Verify style float existence
		// (IE uses styleFloat instead of cssFloat)
		cssFloat: !!a.style.cssFloat,

		// Make sure that if no value is specified for a checkbox
		// that it defaults to "on".
		// (WebKit defaults to "" instead)
		checkOn: ( input.value === "on" ),

		// Make sure that a selected-by-default option has a working selected property.
		// (WebKit defaults to false instead of true, IE too, if it's in an optgroup)
		optSelected: opt.selected,

		// Test setAttribute on camelCase class. If it works, we need attrFixes when doing get/setAttribute (ie6/7)
		getSetAttribute: div.className !== "t",

		// Tests for enctype support on a form(#6743)
		enctype: !!document.createElement("form").enctype,

		// Makes sure cloning an html5 element does not cause problems
		// Where outerHTML is undefined, this still works
		html5Clone: document.createElement("nav").cloneNode( true ).outerHTML !== "<:nav></:nav>",

		// Will be defined later
		submitBubbles: true,
		changeBubbles: true,
		focusinBubbles: false,
		deleteExpando: true,
		noCloneEvent: true,
		inlineBlockNeedsLayout: false,
		shrinkWrapBlocks: false,
		reliableMarginRight: true,
		pixelMargin: true
	};

	// teabo.boxModel DEPRECATED in 1.3, use teabo.support.boxModel instead
	teabo.boxModel = support.boxModel = (document.compatMode === "CSS1Compat");

	// Make sure checked status is properly cloned
	input.checked = true;
	support.noCloneChecked = input.cloneNode( true ).checked;

	// Make sure that the options inside disabled selects aren't marked as disabled
	// (WebKit marks them as disabled)
	select.disabled = true;
	support.optDisabled = !opt.disabled;

	// Test to see if it's possible to delete an expando from an element
	// Fails in Internet Explorer
	try {
		delete div.test;
	} catch( e ) {
		support.deleteExpando = false;
	}

	if ( !div.addEventListener && div.attachEvent && div.fireEvent ) {
		div.attachEvent( "onclick", function() {
			// Cloning a node shouldn't copy over any
			// bound event handlers (IE does this)
			support.noCloneEvent = false;
		});
		div.cloneNode( true ).fireEvent( "onclick" );
	}

	// Check if a radio maintains its value
	// after being appended to the DOM
	input = document.createElement("input");
	input.value = "t";
	input.setAttribute("type", "radio");
	support.radioValue = input.value === "t";

	input.setAttribute("checked", "checked");

	// #11217 - WebKit loses check when the name is after the checked attribute
	input.setAttribute( "name", "t" );

	div.appendChild( input );
	fragment = document.createDocumentFragment();
	fragment.appendChild( div.lastChild );

	// WebKit doesn't clone checked state correctly in fragments
	support.checkClone = fragment.cloneNode( true ).cloneNode( true ).lastChild.checked;

	// Check if a disconnected checkbox will retain its checked
	// value of true after appended to the DOM (IE6/7)
	support.appendChecked = input.checked;

	fragment.removeChild( input );
	fragment.appendChild( div );

	// Technique from Juriy Zaytsev
	// http://perfectionkills.com/detecting-event-support-without-browser-sniffing/
	// We only care about the case where non-standard event systems
	// are used, namely in IE. Short-circuiting here helps us to
	// avoid an eval call (in setAttribute) which can cause CSP
	// to go haywire. See: https://developer.mozilla.org/en/Security/CSP
	if ( div.attachEvent ) {
		for ( i in {
			submit: 1,
			change: 1,
			focusin: 1
		}) {
			eventName = "on" + i;
			isSupported = ( eventName in div );
			if ( !isSupported ) {
				div.setAttribute( eventName, "return;" );
				isSupported = ( typeof div[ eventName ] === "function" );
			}
			support[ i + "Bubbles" ] = isSupported;
		}
	}

	fragment.removeChild( div );

	// Null elements to avoid leaks in IE
	fragment = select = opt = div = input = null;

	// Run tests that need a body at doc ready
	teabo(function() {
		var container, outer, inner, table, td, offsetSupport,
			marginDiv, conMarginTop, style, html, positionTopLeftWidthHeight,
			paddingMarginBorderVisibility, paddingMarginBorder,
			body = document.getElementsByTagName("body")[0];

		if ( !body ) {
			// Return for frameset docs that don't have a body
			return;
		}

		conMarginTop = 1;
		paddingMarginBorder = "padding:0;margin:0;border:";
		positionTopLeftWidthHeight = "position:absolute;top:0;left:0;width:1px;height:1px;";
		paddingMarginBorderVisibility = paddingMarginBorder + "0;visibility:hidden;";
		style = "style='" + positionTopLeftWidthHeight + paddingMarginBorder + "5px solid #000;";
		html = "<div " + style + "display:block;'><div style='" + paddingMarginBorder + "0;display:block;overflow:hidden;'></div></div>" +
			"<table " + style + "' cellpadding='0' cellspacing='0'>" +
			"<tr><td></td></tr></table>";

		container = document.createElement("div");
		container.style.cssText = paddingMarginBorderVisibility + "width:0;height:0;position:static;top:0;margin-top:" + conMarginTop + "px";
		body.insertBefore( container, body.firstChild );

		// Construct the test element
		div = document.createElement("div");
		container.appendChild( div );

		// Check if table cells still have offsetWidth/Height when they are set
		// to display:none and there are still other visible table cells in a
		// table row; if so, offsetWidth/Height are not reliable for use when
		// determining if an element has been hidden directly using
		// display:none (it is still safe to use offsets if a parent element is
		// hidden; don safety goggles and see bug #4512 for more information).
		// (only IE 8 fails this test)
		div.innerHTML = "<table><tr><td style='" + paddingMarginBorder + "0;display:none'></td><td>t</td></tr></table>";
		tds = div.getElementsByTagName( "td" );
		isSupported = ( tds[ 0 ].offsetHeight === 0 );

		tds[ 0 ].style.display = "";
		tds[ 1 ].style.display = "none";

		// Check if empty table cells still have offsetWidth/Height
		// (IE <= 8 fail this test)
		support.reliableHiddenOffsets = isSupported && ( tds[ 0 ].offsetHeight === 0 );

		// Check if div with explicit width and no margin-right incorrectly
		// gets computed margin-right based on width of container. For more
		// info see bug #3333
		// Fails in WebKit before Feb 2011 nightlies
		// WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
		if ( window.getComputedStyle ) {
			div.innerHTML = "";
			marginDiv = document.createElement( "div" );
			marginDiv.style.width = "0";
			marginDiv.style.marginRight = "0";
			div.style.width = "2px";
			div.appendChild( marginDiv );
			support.reliableMarginRight =
				( parseInt( ( window.getComputedStyle( marginDiv, null ) || { marginRight: 0 } ).marginRight, 10 ) || 0 ) === 0;
		}

		if ( typeof div.style.zoom !== "undefined" ) {
			// Check if natively block-level elements act like inline-block
			// elements when setting their display to 'inline' and giving
			// them layout
			// (IE < 8 does this)
			div.innerHTML = "";
			div.style.width = div.style.padding = "1px";
			div.style.border = 0;
			div.style.overflow = "hidden";
			div.style.display = "inline";
			div.style.zoom = 1;
			support.inlineBlockNeedsLayout = ( div.offsetWidth === 3 );

			// Check if elements with layout shrink-wrap their children
			// (IE 6 does this)
			div.style.display = "block";
			div.style.overflow = "visible";
			div.innerHTML = "<div style='width:5px;'></div>";
			support.shrinkWrapBlocks = ( div.offsetWidth !== 3 );
		}

		div.style.cssText = positionTopLeftWidthHeight + paddingMarginBorderVisibility;
		div.innerHTML = html;

		outer = div.firstChild;
		inner = outer.firstChild;
		td = outer.nextSibling.firstChild.firstChild;

		offsetSupport = {
			doesNotAddBorder: ( inner.offsetTop !== 5 ),
			doesAddBorderForTableAndCells: ( td.offsetTop === 5 )
		};

		inner.style.position = "fixed";
		inner.style.top = "20px";

		// safari subtracts parent border width here which is 5px
		offsetSupport.fixedPosition = ( inner.offsetTop === 20 || inner.offsetTop === 15 );
		inner.style.position = inner.style.top = "";

		outer.style.overflow = "hidden";
		outer.style.position = "relative";

		offsetSupport.subtractsBorderForOverflowNotVisible = ( inner.offsetTop === -5 );
		offsetSupport.doesNotIncludeMarginInBodyOffset = ( body.offsetTop !== conMarginTop );

		if ( window.getComputedStyle ) {
			div.style.marginTop = "1%";
			support.pixelMargin = ( window.getComputedStyle( div, null ) || { marginTop: 0 } ).marginTop !== "1%";
		}

		if ( typeof container.style.zoom !== "undefined" ) {
			container.style.zoom = 1;
		}

		body.removeChild( container );
		marginDiv = div = container = null;

		teabo.extend( support, offsetSupport );
	});

	return support;
})();

var rbrace = /^(?:\{.*\}|\[.*\])$/,
rmultiDash = /([A-Z])/g;

teabo.extend({
cache: {},

// Please use with caution
uuid: 0,

// Unique for each copy of teabo on the page
// Non-digits removed to match rinlinejQuery
expando: "teabo" + ( teabo.fn.teabo + Math.random() ).replace( /\D/g, "" ),

// The following elements throw uncatchable exceptions if you
// attempt to add expando properties to them.
noData: {
	"embed": true,
	// Ban all objects except for Flash (which handle expandos)
	"object": "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",
	"applet": true
},

hasData: function( elem ) {
	elem = elem.nodeType ? teabo.cache[ elem[teabo.expando] ] : elem[ teabo.expando ];
	return !!elem && !isEmptyDataObject( elem );
},

data: function( elem, name, data, pvt /* Internal Use Only */ ) {
	if ( !teabo.acceptData( elem ) ) {
		return;
	}

	var privateCache, thisCache, ret,
		internalKey = teabo.expando,
		getByName = typeof name === "string",

		// We have to handle DOM nodes and JS objects differently because IE6-7
		// can't GC object references properly across the DOM-JS boundary
		isNode = elem.nodeType,

		// Only DOM nodes need the global teabo cache; JS object data is
		// attached directly to the object so GC can occur automatically
		cache = isNode ? teabo.cache : elem,

		// Only defining an ID for JS objects if its cache already exists allows
		// the code to shortcut on the same path as a DOM node with no cache
		id = isNode ? elem[ internalKey ] : elem[ internalKey ] && internalKey,
		isEvents = name === "events";

	// Avoid doing any more work than we need to when trying to get data on an
	// object that has no data at all
	if ( (!id || !cache[id] || (!isEvents && !pvt && !cache[id].data)) && getByName && data === undefined ) {
		return;
	}

	if ( !id ) {
		// Only DOM nodes need a new unique ID for each element since their data
		// ends up in the global cache
		if ( isNode ) {
			elem[ internalKey ] = id = ++teabo.uuid;
		} else {
			id = internalKey;
		}
	}

	if ( !cache[ id ] ) {
		cache[ id ] = {};

		// Avoids exposing teabo metadata on plain JS objects when the object
		// is serialized using JSON.stringify
		if ( !isNode ) {
			cache[ id ].toJSON = teabo.noop;
		}
	}

	// An object can be passed to teabo.data instead of a key/value pair; this gets
	// shallow copied over onto the existing cache
	if ( typeof name === "object" || typeof name === "function" ) {
		if ( pvt ) {
			cache[ id ] = teabo.extend( cache[ id ], name );
		} else {
			cache[ id ].data = teabo.extend( cache[ id ].data, name );
		}
	}

	privateCache = thisCache = cache[ id ];

	// teabo data() is stored in a separate object inside the object's internal data
	// cache in order to avoid key collisions between internal data and user-defined
	// data.
	if ( !pvt ) {
		if ( !thisCache.data ) {
			thisCache.data = {};
		}

		thisCache = thisCache.data;
	}

	if ( data !== undefined ) {
		thisCache[ teabo.camelCase( name ) ] = data;
	}

	// Users should not attempt to inspect the internal events object using teabo.data,
	// it is undocumented and subject to change. But does anyone listen? No.
	if ( isEvents && !thisCache[ name ] ) {
		return privateCache.events;
	}

	// Check for both converted-to-camel and non-converted data property names
	// If a data property was specified
	if ( getByName ) {

		// First Try to find as-is property data
		ret = thisCache[ name ];

		// Test for null|undefined property data
		if ( ret == null ) {

			// Try to find the camelCased property
			ret = thisCache[ teabo.camelCase( name ) ];
		}
	} else {
		ret = thisCache;
	}

	return ret;
},

removeData: function( elem, name, pvt /* Internal Use Only */ ) {
	if ( !teabo.acceptData( elem ) ) {
		return;
	}

	var thisCache, i, l,

		// Reference to internal data cache key
		internalKey = teabo.expando,

		isNode = elem.nodeType,

		// See teabo.data for more information
		cache = isNode ? teabo.cache : elem,

		// See teabo.data for more information
		id = isNode ? elem[ internalKey ] : internalKey;

	// If there is already no cache entry for this object, there is no
	// purpose in continuing
	if ( !cache[ id ] ) {
		return;
	}

	if ( name ) {

		thisCache = pvt ? cache[ id ] : cache[ id ].data;

		if ( thisCache ) {

			// Support array or space separated string names for data keys
			if ( !teabo.isArray( name ) ) {

				// try the string as a key before any manipulation
				if ( name in thisCache ) {
					name = [ name ];
				} else {

					// split the camel cased version by spaces unless a key with the spaces exists
					name = teabo.camelCase( name );
					if ( name in thisCache ) {
						name = [ name ];
					} else {
						name = name.split( " " );
					}
				}
			}

			for ( i = 0, l = name.length; i < l; i++ ) {
				delete thisCache[ name[i] ];
			}

			// If there is no data left in the cache, we want to continue
			// and let the cache object itself get destroyed
			if ( !( pvt ? isEmptyDataObject : teabo.isEmptyObject )( thisCache ) ) {
				return;
			}
		}
	}

	// See teabo.data for more information
	if ( !pvt ) {
		delete cache[ id ].data;

		// Don't destroy the parent cache unless the internal data object
		// had been the only thing left in it
		if ( !isEmptyDataObject(cache[ id ]) ) {
			return;
		}
	}

	// Browsers that fail expando deletion also refuse to delete expandos on
	// the window, but it will allow it on all other JS objects; other browsers
	// don't care
	// Ensure that `cache` is not a window object #10080
	if ( teabo.support.deleteExpando || !cache.setInterval ) {
		delete cache[ id ];
	} else {
		cache[ id ] = null;
	}

	// We destroyed the cache and need to eliminate the expando on the node to avoid
	// false lookups in the cache for entries that no longer exist
	if ( isNode ) {
		// IE does not allow us to delete expando properties from nodes,
		// nor does it have a removeAttribute function on Document nodes;
		// we must handle all of these cases
		if ( teabo.support.deleteExpando ) {
			delete elem[ internalKey ];
		} else if ( elem.removeAttribute ) {
			elem.removeAttribute( internalKey );
		} else {
			elem[ internalKey ] = null;
		}
	}
},

// For internal use only.
_data: function( elem, name, data ) {
	return teabo.data( elem, name, data, true );
},

// A method for determining if a DOM node can handle the data expando
acceptData: function( elem ) {
	if ( elem.nodeName ) {
		var match = teabo.noData[ elem.nodeName.toLowerCase() ];

		if ( match ) {
			return !(match === true || elem.getAttribute("classid") !== match);
		}
	}

	return true;
}
});

teabo.fn.extend({
data: function( key, value ) {
	var parts, part, attr, name, l,
		elem = this[0],
		i = 0,
		data = null;

	// Gets all values
	if ( key === undefined ) {
		if ( this.length ) {
			data = teabo.data( elem );

			if ( elem.nodeType === 1 && !teabo._data( elem, "parsedAttrs" ) ) {
				attr = elem.attributes;
				for ( l = attr.length; i < l; i++ ) {
					name = attr[i].name;

					if ( name.indexOf( "data-" ) === 0 ) {
						name = teabo.camelCase( name.substring(5) );

						dataAttr( elem, name, data[ name ] );
					}
				}
				teabo._data( elem, "parsedAttrs", true );
			}
		}

		return data;
	}

	// Sets multiple values
	if ( typeof key === "object" ) {
		return this.each(function() {
			teabo.data( this, key );
		});
	}

	parts = key.split( ".", 2 );
	parts[1] = parts[1] ? "." + parts[1] : "";
	part = parts[1] + "!";

	return teabo.access( this, function( value ) {

		if ( value === undefined ) {
			data = this.triggerHandler( "getData" + part, [ parts[0] ] );

			// Try to fetch any internally stored data first
			if ( data === undefined && elem ) {
				data = teabo.data( elem, key );
				data = dataAttr( elem, key, data );
			}

			return data === undefined && parts[1] ?
				this.data( parts[0] ) :
				data;
		}

		parts[1] = value;
		this.each(function() {
			var self = teabo( this );

			self.triggerHandler( "setData" + part, parts );
			teabo.data( this, key, value );
			self.triggerHandler( "changeData" + part, parts );
		});
	}, null, value, arguments.length > 1, null, false );
},

removeData: function( key ) {
	return this.each(function() {
		teabo.removeData( this, key );
	});
}
});

function dataAttr( elem, key, data ) {
// If nothing was found internally, try to fetch any
// data from the HTML5 data-* attribute
if ( data === undefined && elem.nodeType === 1 ) {

	var name = "data-" + key.replace( rmultiDash, "-$1" ).toLowerCase();

	data = elem.getAttribute( name );

	if ( typeof data === "string" ) {
		try {
			data = data === "true" ? true :
			data === "false" ? false :
			data === "null" ? null :
			teabo.isNumeric( data ) ? +data :
				rbrace.test( data ) ? teabo.parseJSON( data ) :
				data;
		} catch( e ) {}

		// Make sure we set the data so it isn't changed later
		teabo.data( elem, key, data );

	} else {
		data = undefined;
	}
}

return data;
}

//checks a cache object for emptiness
function isEmptyDataObject( obj ) {
for ( var name in obj ) {

	// if the public data object is empty, the private is still empty
	if ( name === "data" && teabo.isEmptyObject( obj[name] ) ) {
		continue;
	}
	if ( name !== "toJSON" ) {
		return false;
	}
}

return true;
}




function handleQueueMarkDefer( elem, type, src ) {
var deferDataKey = type + "defer",
	queueDataKey = type + "queue",
	markDataKey = type + "mark",
	defer = teabo._data( elem, deferDataKey );
if ( defer &&
	( src === "queue" || !teabo._data(elem, queueDataKey) ) &&
	( src === "mark" || !teabo._data(elem, markDataKey) ) ) {
	// Give room for hard-coded callbacks to fire first
	// and eventually mark/queue something else on the element
	setTimeout( function() {
		if ( !teabo._data( elem, queueDataKey ) &&
			!teabo._data( elem, markDataKey ) ) {
			teabo.removeData( elem, deferDataKey, true );
			defer.fire();
		}
	}, 0 );
}
}

teabo.extend({

_mark: function( elem, type ) {
	if ( elem ) {
		type = ( type || "fx" ) + "mark";
		teabo._data( elem, type, (teabo._data( elem, type ) || 0) + 1 );
	}
},

_unmark: function( force, elem, type ) {
	if ( force !== true ) {
		type = elem;
		elem = force;
		force = false;
	}
	if ( elem ) {
		type = type || "fx";
		var key = type + "mark",
			count = force ? 0 : ( (teabo._data( elem, key ) || 1) - 1 );
		if ( count ) {
			teabo._data( elem, key, count );
		} else {
			teabo.removeData( elem, key, true );
			handleQueueMarkDefer( elem, type, "mark" );
		}
	}
},

queue: function( elem, type, data ) {
	var q;
	if ( elem ) {
		type = ( type || "fx" ) + "queue";
		q = teabo._data( elem, type );

		// Speed up dequeue by getting out quickly if this is just a lookup
		if ( data ) {
			if ( !q || teabo.isArray(data) ) {
				q = teabo._data( elem, type, teabo.makeArray(data) );
			} else {
				q.push( data );
			}
		}
		return q || [];
	}
},

dequeue: function( elem, type ) {
	type = type || "fx";

	var queue = teabo.queue( elem, type ),
		fn = queue.shift(),
		hooks = {};

	// If the fx queue is dequeued, always remove the progress sentinel
	if ( fn === "inprogress" ) {
		fn = queue.shift();
	}

	if ( fn ) {
		// Add a progress sentinel to prevent the fx queue from being
		// automatically dequeued
		if ( type === "fx" ) {
			queue.unshift( "inprogress" );
		}

		teabo._data( elem, type + ".run", hooks );
		fn.call( elem, function() {
			teabo.dequeue( elem, type );
		}, hooks );
	}

	if ( !queue.length ) {
		teabo.removeData( elem, type + "queue " + type + ".run", true );
		handleQueueMarkDefer( elem, type, "queue" );
	}
}
});

teabo.fn.extend({
queue: function( type, data ) {
	var setter = 2;

	if ( typeof type !== "string" ) {
		data = type;
		type = "fx";
		setter--;
	}

	if ( arguments.length < setter ) {
		return teabo.queue( this[0], type );
	}

	return data === undefined ?
		this :
		this.each(function() {
			var queue = teabo.queue( this, type, data );

			if ( type === "fx" && queue[0] !== "inprogress" ) {
				teabo.dequeue( this, type );
			}
		});
},
dequeue: function( type ) {
	return this.each(function() {
		teabo.dequeue( this, type );
	});
},
// Based off of the plugin by Clint Helfers, with permission.
// http://blindsignals.com/index.php/2009/07/teabo-delay/
delay: function( time, type ) {
	time = teabo.fx ? teabo.fx.speeds[ time ] || time : time;
	type = type || "fx";

	return this.queue( type, function( next, hooks ) {
		var timeout = setTimeout( next, time );
		hooks.stop = function() {
			clearTimeout( timeout );
		};
	});
},
clearQueue: function( type ) {
	return this.queue( type || "fx", [] );
},
// Get a promise resolved when queues of a certain type
// are emptied (fx is the type by default)
promise: function( type, object ) {
	if ( typeof type !== "string" ) {
		object = type;
		type = undefined;
	}
	type = type || "fx";
	var defer = teabo.Deferred(),
		elements = this,
		i = elements.length,
		count = 1,
		deferDataKey = type + "defer",
		queueDataKey = type + "queue",
		markDataKey = type + "mark",
		tmp;
	function resolve() {
		if ( !( --count ) ) {
			defer.resolveWith( elements, [ elements ] );
		}
	}
	while( i-- ) {
		if (( tmp = teabo.data( elements[ i ], deferDataKey, undefined, true ) ||
				( teabo.data( elements[ i ], queueDataKey, undefined, true ) ||
					teabo.data( elements[ i ], markDataKey, undefined, true ) ) &&
				teabo.data( elements[ i ], deferDataKey, teabo.Callbacks( "once memory" ), true ) )) {
			count++;
			tmp.add( resolve );
		}
	}
	resolve();
	return defer.promise( object );
}
});


var rclass = /[\n\t\r]/g,
rspace = /\s+/,
rreturn = /\r/g,
rtype = /^(?:button|input)$/i,
rfocusable = /^(?:button|input|object|select|textarea)$/i,
rclickable = /^a(?:rea)?$/i,
rboolean = /^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i,
getSetAttribute = teabo.support.getSetAttribute,
nodeHook, boolHook, fixSpecified;

teabo.fn.extend({
	attr: function( name, value ) {
		return teabo.access( this, teabo.attr, name, value, arguments.length > 1 );
	},

	removeAttr: function( name ) {
		return this.each(function() {
			teabo.removeAttr( this, name );
		});
	},

	prop: function( name, value ) {
		return teabo.access( this, teabo.prop, name, value, arguments.length > 1 );
	},

	removeProp: function( name ) {
		name = teabo.propFix[ name ] || name;
		return this.each(function() {
			// try/catch handles cases where IE balks (such as removing a property on window)
			try {
				this[ name ] = undefined;
				delete this[ name ];
			} catch( e ) {}
		});
	},

	addClass: function( value ) {
		var classNames, i, l, elem,
		setClass, c, cl;

		if ( teabo.isFunction( value ) ) {
			return this.each(function( j ) {
				teabo( this ).addClass( value.call(this, j, this.className) );
			});
		}

		if ( value && typeof value === "string" ) {
			classNames = value.split( rspace );

			for ( i = 0, l = this.length; i < l; i++ ) {
				elem = this[ i ];

				if ( elem.nodeType === 1 ) {
					if ( !elem.className && classNames.length === 1 ) {
						elem.className = value;

					} else {
						setClass = " " + elem.className + " ";

						for ( c = 0, cl = classNames.length; c < cl; c++ ) {
							if ( !~setClass.indexOf( " " + classNames[ c ] + " " ) ) {
								setClass += classNames[ c ] + " ";
							}
						}
						elem.className = teabo.trim( setClass );
					}
				}
			}
		}

		return this;
	},

	removeClass: function( value ) {
		var classNames, i, l, elem, className, c, cl;

		if ( teabo.isFunction( value ) ) {
			return this.each(function( j ) {
				teabo( this ).removeClass( value.call(this, j, this.className) );
			});
		}

		if ( (value && typeof value === "string") || value === undefined ) {
			classNames = ( value || "" ).split( rspace );

			for ( i = 0, l = this.length; i < l; i++ ) {
				elem = this[ i ];

				if ( elem.nodeType === 1 && elem.className ) {
					if ( value ) {
						className = (" " + elem.className + " ").replace( rclass, " " );
						for ( c = 0, cl = classNames.length; c < cl; c++ ) {
							className = className.replace(" " + classNames[ c ] + " ", " ");
						}
						elem.className = teabo.trim( className );

					} else {
						elem.className = "";
					}
				}
			}
		}

		return this;
	},

	toggleClass: function( value, stateVal ) {
		var type = typeof value,
		isBool = typeof stateVal === "boolean";

		if ( teabo.isFunction( value ) ) {
			return this.each(function( i ) {
				teabo( this ).toggleClass( value.call(this, i, this.className, stateVal), stateVal );
			});
		}

		return this.each(function() {
			if ( type === "string" ) {
				// toggle individual class names
				var className,
				i = 0,
				self = teabo( this ),
				state = stateVal,
				classNames = value.split( rspace );

				while ( (className = classNames[ i++ ]) ) {
					// check each className given, space seperated list
					state = isBool ? state : !self.hasClass( className );
					self[ state ? "addClass" : "removeClass" ]( className );
				}

			} else if ( type === "undefined" || type === "boolean" ) {
				if ( this.className ) {
					// store className if set
					teabo._data( this, "__className__", this.className );
				}

				// toggle whole className
				this.className = this.className || value === false ? "" : teabo._data( this, "__className__" ) || "";
			}
		});
	},

	hasClass: function( selector ) {
		var className = " " + selector + " ",
		i = 0,
		l = this.length;
		for ( ; i < l; i++ ) {
			if ( this[i].nodeType === 1 && (" " + this[i].className + " ").replace(rclass, " ").indexOf( className ) > -1 ) {
				return true;
			}
		}

		return false;
	},

	val: function( value ) {
		var hooks, ret, isFunction,
		elem = this[0];

		if ( !arguments.length ) {
			if ( elem ) {
				hooks = teabo.valHooks[ elem.type ] || teabo.valHooks[ elem.nodeName.toLowerCase() ];

				if ( hooks && "get" in hooks && (ret = hooks.get( elem, "value" )) !== undefined ) {
					return ret;
				}

				ret = elem.value;

				return typeof ret === "string" ?
						// handle most common string cases
						ret.replace(rreturn, "") :
							// handle cases where value is null/undef or number
							ret == null ? "" : ret;
			}

			return;
		}

		isFunction = teabo.isFunction( value );

		return this.each(function( i ) {
			var self = teabo(this), val;

			if ( this.nodeType !== 1 ) {
				return;
			}

			if ( isFunction ) {
				val = value.call( this, i, self.val() );
			} else {
				val = value;
			}

			// Treat null/undefined as ""; convert numbers to string
			if ( val == null ) {
				val = "";
			} else if ( typeof val === "number" ) {
				val += "";
			} else if ( teabo.isArray( val ) ) {
				val = teabo.map(val, function ( value ) {
					return value == null ? "" : value + "";
				});
			}

			hooks = teabo.valHooks[ this.type ] || teabo.valHooks[ this.nodeName.toLowerCase() ];

			// If set returns undefined, fall back to normal setting
			if ( !hooks || !("set" in hooks) || hooks.set( this, val, "value" ) === undefined ) {
				this.value = val;
			}
		});
	}
});

teabo.extend({
	valHooks: {
		option: {
			get: function( elem ) {
				// attributes.value is undefined in Blackberry 4.7 but
				// uses .value. See #6932
				var val = elem.attributes.value;
				return !val || val.specified ? elem.value : elem.text;
			}
		},
		select: {
			get: function( elem ) {
				var value, i, max, option,
				index = elem.selectedIndex,
				values = [],
				options = elem.options,
				one = elem.type === "select-one";

				// Nothing was selected
				if ( index < 0 ) {
					return null;
				}

				// Loop through all the selected options
				i = one ? index : 0;
				max = one ? index + 1 : options.length;
				for ( ; i < max; i++ ) {
					option = options[ i ];

					// Don't return options that are disabled or in a disabled optgroup
					if ( option.selected && (teabo.support.optDisabled ? !option.disabled : option.getAttribute("disabled") === null) &&
							(!option.parentNode.disabled || !teabo.nodeName( option.parentNode, "optgroup" )) ) {

						// Get the specific value for the option
						value = teabo( option ).val();

						// We don't need an array for one selects
						if ( one ) {
							return value;
						}

						// Multi-Selects return an array
						values.push( value );
					}
				}

				// Fixes Bug #2551 -- select.val() broken in IE after form.reset()
				if ( one && !values.length && options.length ) {
					return teabo( options[ index ] ).val();
				}

				return values;
			},

			set: function( elem, value ) {
				var values = teabo.makeArray( value );

				teabo(elem).find("option").each(function() {
					this.selected = teabo.inArray( teabo(this).val(), values ) >= 0;
				});

				if ( !values.length ) {
					elem.selectedIndex = -1;
				}
				return values;
			}
		}
	},

	attrFn: {
		val: true,
		css: true,
		html: true,
		text: true,
		data: true,
		width: true,
		height: true,
		offset: true
	},

	attr: function( elem, name, value, pass ) {
		var ret, hooks, notxml,
		nType = elem.nodeType;

		// don't get/set attributes on text, comment and attribute nodes
		if ( !elem || nType === 3 || nType === 8 || nType === 2 ) {
			return;
		}

		if ( pass && name in teabo.attrFn ) {
			return teabo( elem )[ name ]( value );
		}

		// Fallback to prop when attributes are not supported
		if ( typeof elem.getAttribute === "undefined" ) {
			return teabo.prop( elem, name, value );
		}

		notxml = nType !== 1 || !teabo.isXMLDoc( elem );

		// All attributes are lowercase
		// Grab necessary hook if one is defined
		if ( notxml ) {
			name = name.toLowerCase();
			hooks = teabo.attrHooks[ name ] || ( rboolean.test( name ) ? boolHook : nodeHook );
		}

		if ( value !== undefined ) {

			if ( value === null ) {
				teabo.removeAttr( elem, name );
				return;

			} else if ( hooks && "set" in hooks && notxml && (ret = hooks.set( elem, value, name )) !== undefined ) {
				return ret;

			} else {
				elem.setAttribute( name, "" + value );
				return value;
			}

		} else if ( hooks && "get" in hooks && notxml && (ret = hooks.get( elem, name )) !== null ) {
			return ret;

		} else {

			ret = elem.getAttribute( name );

			// Non-existent attributes return null, we normalize to undefined
			return ret === null ?
					undefined :
						ret;
		}
	},

	removeAttr: function( elem, value ) {
		var propName, attrNames, name, l, isBool,
		i = 0;

		if ( value && elem.nodeType === 1 ) {
			attrNames = value.toLowerCase().split( rspace );
			l = attrNames.length;

			for ( ; i < l; i++ ) {
				name = attrNames[ i ];

				if ( name ) {
					propName = teabo.propFix[ name ] || name;
					isBool = rboolean.test( name );

					// See #9699 for explanation of this approach (setting first, then removal)
					// Do not do this for boolean attributes (see #10870)
					if ( !isBool ) {
						teabo.attr( elem, name, "" );
					}
					elem.removeAttribute( getSetAttribute ? name : propName );

					// Set corresponding property to false for boolean attributes
					if ( isBool && propName in elem ) {
						elem[ propName ] = false;
					}
				}
			}
		}
	},

	attrHooks: {
		type: {
			set: function( elem, value ) {
				// We can't allow the type property to be changed (since it causes problems in IE)
				if ( rtype.test( elem.nodeName ) && elem.parentNode ) {
					teabo.error( "type property can't be changed" );
				} else if ( !teabo.support.radioValue && value === "radio" && teabo.nodeName(elem, "input") ) {
					// Setting the type on a radio button after the value resets the value in IE6-9
					// Reset value to it's default in case type is set after value
					// This is for element creation
					var val = elem.value;
					elem.setAttribute( "type", value );
					if ( val ) {
						elem.value = val;
					}
					return value;
				}
			}
		},
		// Use the value property for back compat
		// Use the nodeHook for button elements in IE6/7 (#1954)
		value: {
			get: function( elem, name ) {
				if ( nodeHook && teabo.nodeName( elem, "button" ) ) {
					return nodeHook.get( elem, name );
				}
				return name in elem ?
						elem.value :
							null;
			},
			set: function( elem, value, name ) {
				if ( nodeHook && teabo.nodeName( elem, "button" ) ) {
					return nodeHook.set( elem, value, name );
				}
				// Does not return so that setAttribute is also used
				elem.value = value;
			}
		}
	},

	propFix: {
		tabindex: "tabIndex",
		readonly: "readOnly",
		"for": "htmlFor",
		"class": "className",
		maxlength: "maxLength",
		cellspacing: "cellSpacing",
		cellpadding: "cellPadding",
		rowspan: "rowSpan",
		colspan: "colSpan",
		usemap: "useMap",
		frameborder: "frameBorder",
		contenteditable: "contentEditable"
	},

	prop: function( elem, name, value ) {
		var ret, hooks, notxml,
		nType = elem.nodeType;

		// don't get/set properties on text, comment and attribute nodes
		if ( !elem || nType === 3 || nType === 8 || nType === 2 ) {
			return;
		}

		notxml = nType !== 1 || !teabo.isXMLDoc( elem );

		if ( notxml ) {
			// Fix name and attach hooks
			name = teabo.propFix[ name ] || name;
			hooks = teabo.propHooks[ name ];
		}

		if ( value !== undefined ) {
			if ( hooks && "set" in hooks && (ret = hooks.set( elem, value, name )) !== undefined ) {
				return ret;

			} else {
				return ( elem[ name ] = value );
			}

		} else {
			if ( hooks && "get" in hooks && (ret = hooks.get( elem, name )) !== null ) {
				return ret;

			} else {
				return elem[ name ];
			}
		}
	},

	propHooks: {
		tabIndex: {
			get: function( elem ) {
				// elem.tabIndex doesn't always return the correct value when it hasn't been explicitly set
				// http://fluidproject.org/blog/2008/01/09/getting-setting-and-removing-tabindex-values-with-javascript/
				var attributeNode = elem.getAttributeNode("tabindex");

				return attributeNode && attributeNode.specified ?
						parseInt( attributeNode.value, 10 ) :
							rfocusable.test( elem.nodeName ) || rclickable.test( elem.nodeName ) && elem.href ?
									0 :
										undefined;
			}
		}
	}
});

//Add the tabIndex propHook to attrHooks for back-compat (different case is intentional)
teabo.attrHooks.tabindex = teabo.propHooks.tabIndex;

//Hook for boolean attributes
boolHook = {
		get: function( elem, name ) {
			// Align boolean attributes with corresponding properties
			// Fall back to attribute presence where some booleans are not supported
			var attrNode,
			property = teabo.prop( elem, name );
			return property === true || typeof property !== "boolean" && ( attrNode = elem.getAttributeNode(name) ) && attrNode.nodeValue !== false ?
					name.toLowerCase() :
						undefined;
		},
		set: function( elem, value, name ) {
			var propName;
			if ( value === false ) {
				// Remove boolean attributes when set to false
				teabo.removeAttr( elem, name );
			} else {
				// value is true since we know at this point it's type boolean and not false
				// Set boolean attributes to the same name and set the DOM property
				propName = teabo.propFix[ name ] || name;
				if ( propName in elem ) {
					// Only set the IDL specifically if it already exists on the element
					elem[ propName ] = true;
				}

				elem.setAttribute( name, name.toLowerCase() );
			}
			return name;
		}
};

//IE6/7 do not support getting/setting some attributes with get/setAttribute
if ( !getSetAttribute ) {

	fixSpecified = {
			name: true,
			id: true,
			coords: true
	};

//	Use this for any attribute in IE6/7
//	This fixes almost every IE6/7 issue
	nodeHook = teabo.valHooks.button = {
			get: function( elem, name ) {
				var ret;
				ret = elem.getAttributeNode( name );
				return ret && ( fixSpecified[ name ] ? ret.nodeValue !== "" : ret.specified ) ?
						ret.nodeValue :
							undefined;
			},
			set: function( elem, value, name ) {
				// Set the existing or create a new attribute node
				var ret = elem.getAttributeNode( name );
				if ( !ret ) {
					ret = document.createAttribute( name );
					elem.setAttributeNode( ret );
				}
				return ( ret.nodeValue = value + "" );
			}
	};

//	Apply the nodeHook to tabindex
	teabo.attrHooks.tabindex.set = nodeHook.set;

//	Set width and height to auto instead of 0 on empty string( Bug #8150 )
//	This is for removals
	teabo.each([ "width", "height" ], function( i, name ) {
		teabo.attrHooks[ name ] = teabo.extend( teabo.attrHooks[ name ], {
			set: function( elem, value ) {
				if ( value === "" ) {
					elem.setAttribute( name, "auto" );
					return value;
				}
			}
		});
	});

//	Set contenteditable to false on removals(#10429)
//	Setting to empty string throws an error as an invalid value
	teabo.attrHooks.contenteditable = {
			get: nodeHook.get,
			set: function( elem, value, name ) {
				if ( value === "" ) {
					value = "false";
				}
				nodeHook.set( elem, value, name );
			}
	};
}


//Some attributes require a special call on IE
if ( !teabo.support.hrefNormalized ) {
	teabo.each([ "href", "src", "width", "height" ], function( i, name ) {
		teabo.attrHooks[ name ] = teabo.extend( teabo.attrHooks[ name ], {
			get: function( elem ) {
				var ret = elem.getAttribute( name, 2 );
				return ret === null ? undefined : ret;
			}
		});
	});
}

if ( !teabo.support.style ) {
	teabo.attrHooks.style = {
			get: function( elem ) {
				// Return undefined in the case of empty string
				// Normalize to lowercase since IE uppercases css property names
				return elem.style.cssText.toLowerCase() || undefined;
			},
			set: function( elem, value ) {
				return ( elem.style.cssText = "" + value );
			}
	};
}

//Safari mis-reports the default selected property of an option
//Accessing the parent's selectedIndex property fixes it
if ( !teabo.support.optSelected ) {
	teabo.propHooks.selected = teabo.extend( teabo.propHooks.selected, {
		get: function( elem ) {
			var parent = elem.parentNode;

			if ( parent ) {
				parent.selectedIndex;

				// Make sure that it also works with optgroups, see #5701
				if ( parent.parentNode ) {
					parent.parentNode.selectedIndex;
				}
			}
			return null;
		}
	});
}

//IE6/7 call enctype encoding
if ( !teabo.support.enctype ) {
	teabo.propFix.enctype = "encoding";
}

//Radios and checkboxes getter/setter
if ( !teabo.support.checkOn ) {
	teabo.each([ "radio", "checkbox" ], function() {
		teabo.valHooks[ this ] = {
				get: function( elem ) {
					// Handle the case where in Webkit "" is returned instead of "on" if a value isn't specified
					return elem.getAttribute("value") === null ? "on" : elem.value;
				}
		};
	});
}
teabo.each([ "radio", "checkbox" ], function() {
	teabo.valHooks[ this ] = teabo.extend( teabo.valHooks[ this ], {
		set: function( elem, value ) {
			if ( teabo.isArray( value ) ) {
				return ( elem.checked = teabo.inArray( teabo(elem).val(), value ) >= 0 );
			}
		}
	});
});

var elemdisplay = {},
iframe, iframeDoc,
rfxtypes = /^(?:toggle|show|hide)$/,
rfxnum = /^([+\-]=)?([\d+.\-]+)([a-z%]*)$/i,
timerId,
fxAttrs = [
	// height animations
	[ "height", "marginTop", "marginBottom", "paddingTop", "paddingBottom" ],
	// width animations
	[ "width", "marginLeft", "marginRight", "paddingLeft", "paddingRight" ],
	// opacity animations
	[ "opacity" ]
],
fxNow;

teabo.fn.extend({
	show: function( speed, easing, callback ) {
		var elem, display;

		if ( speed || speed === 0 ) {
			return this.animate( genFx("show", 3), speed, easing, callback );

		} else {
			for ( var i = 0, j = this.length; i < j; i++ ) {
				elem = this[ i ];

				if ( elem.style ) {
					display = elem.style.display;

					// Reset the inline display of this element to learn if it is
					// being hidden by cascaded rules or not
					if ( !teabo._data(elem, "olddisplay") && display === "none" ) {
						display = elem.style.display = "";
					}

					// Set elements which have been overridden with display: none
					// in a stylesheet to whatever the default browser style is
					// for such an element
					if ( (display === "" && teabo.css(elem, "display") === "none") ||
							!teabo.contains( elem.ownerDocument.documentElement, elem ) ) {
						teabo._data( elem, "olddisplay", defaultDisplay(elem.nodeName) );
					}
				}
			}

			// Set the display of most of the elements in a second loop
			// to avoid the constant reflow
			for ( i = 0; i < j; i++ ) {
				elem = this[ i ];

				if ( elem.style ) {
					display = elem.style.display;

					if ( display === "" || display === "none" ) {
						elem.style.display = teabo._data( elem, "olddisplay" ) || "";
					}
				}
			}

			return this;
		}
	},

	hide: function( speed, easing, callback ) {
		if ( speed || speed === 0 ) {
			return this.animate( genFx("hide", 3), speed, easing, callback);

		} else {
			var elem, display,
			i = 0,
			j = this.length;

			for ( ; i < j; i++ ) {
				elem = this[i];
				if ( elem.style ) {
					display = teabo.css( elem, "display" );

					if ( display !== "none" && !teabo._data( elem, "olddisplay" ) ) {
						teabo._data( elem, "olddisplay", display );
					}
				}
			}

			// Set the display of the elements in a second loop
			// to avoid the constant reflow
			for ( i = 0; i < j; i++ ) {
				if ( this[i].style ) {
					this[i].style.display = "none";
				}
			}

			return this;
		}
	},

//	Save the old toggle function
	_toggle: teabo.fn.toggle,

	toggle: function( fn, fn2, callback ) {
		var bool = typeof fn === "boolean";

		if ( teabo.isFunction(fn) && teabo.isFunction(fn2) ) {
			this._toggle.apply( this, arguments );

		} else if ( fn == null || bool ) {
			this.each(function() {
				var state = bool ? fn : teabo(this).is(":hidden");
				teabo(this)[ state ? "show" : "hide" ]();
			});

		} else {
			this.animate(genFx("toggle", 3), fn, fn2, callback);
		}

		return this;
	},

	fadeTo: function( speed, to, easing, callback ) {
		return this.filter(":hidden").css("opacity", 0).show().end()
		.animate({opacity: to}, speed, easing, callback);
	},

	animate: function( prop, speed, easing, callback ) {
		var optall = teabo.speed( speed, easing, callback );

		if ( teabo.isEmptyObject( prop ) ) {
			return this.each( optall.complete, [ false ] );
		}

		// Do not change referenced properties as per-property easing will be lost
		prop = teabo.extend( {}, prop );

		function doAnimation() {
			// XXX 'this' does not always have a nodeName when running the
			// test suite

			if ( optall.queue === false ) {
				teabo._mark( this );
			}

			var opt = teabo.extend( {}, optall ),
			isElement = this.nodeType === 1,
			hidden = isElement && teabo(this).is(":hidden"),
			name, val, p, e, hooks, replace,
			parts, start, end, unit,
			method;

			// will store per property easing and be used to determine when an animation is complete
			opt.animatedProperties = {};

			// first pass over propertys to expand / normalize
			for ( p in prop ) {
				name = teabo.camelCase( p );
				if ( p !== name ) {
					prop[ name ] = prop[ p ];
					delete prop[ p ];
				}

				if ( ( hooks = teabo.cssHooks[ name ] ) && "expand" in hooks ) {
					replace = hooks.expand( prop[ name ] );
					delete prop[ name ];

					// not quite $.extend, this wont overwrite keys already present.
					// also - reusing 'p' from above because we have the correct "name"
					for ( p in replace ) {
						if ( ! ( p in prop ) ) {
							prop[ p ] = replace[ p ];
						}
					}
				}
			}

			for ( name in prop ) {
				val = prop[ name ];
				// easing resolution: per property > opt.specialEasing > opt.easing > 'swing' (default)
				if ( teabo.isArray( val ) ) {
					opt.animatedProperties[ name ] = val[ 1 ];
					val = prop[ name ] = val[ 0 ];
				} else {
					opt.animatedProperties[ name ] = opt.specialEasing && opt.specialEasing[ name ] || opt.easing || 'swing';
				}

				if ( val === "hide" && hidden || val === "show" && !hidden ) {
					return opt.complete.call( this );
				}

				if ( isElement && ( name === "height" || name === "width" ) ) {
					// Make sure that nothing sneaks out
					// Record all 3 overflow attributes because IE does not
					// change the overflow attribute when overflowX and
					// overflowY are set to the same value
					opt.overflow = [ this.style.overflow, this.style.overflowX, this.style.overflowY ];

					// Set display property to inline-block for height/width
					// animations on inline elements that are having width/height animated
					if ( teabo.css( this, "display" ) === "inline" &&
							teabo.css( this, "float" ) === "none" ) {

						// inline-level elements accept inline-block;
						// block-level elements need to be inline with layout
						if ( !teabo.support.inlineBlockNeedsLayout || defaultDisplay( this.nodeName ) === "inline" ) {
							this.style.display = "inline-block";

						} else {
							this.style.zoom = 1;
						}
					}
				}
			}

			if ( opt.overflow != null ) {
				this.style.overflow = "hidden";
			}

			for ( p in prop ) {
				e = new teabo.fx( this, opt, p );
				val = prop[ p ];

				if ( rfxtypes.test( val ) ) {

					// Tracks whether to show or hide based on private
					// data attached to the element
					method = teabo._data( this, "toggle" + p ) || ( val === "toggle" ? hidden ? "show" : "hide" : 0 );
					if ( method ) {
						teabo._data( this, "toggle" + p, method === "show" ? "hide" : "show" );
						e[ method ]();
					} else {
						e[ val ]();
					}

				} else {
					parts = rfxnum.exec( val );
					start = e.cur();

					if ( parts ) {
						end = parseFloat( parts[2] );
						unit = parts[3] || ( teabo.cssNumber[ p ] ? "" : "px" );

						// We need to compute starting value
						if ( unit !== "px" ) {
							teabo.style( this, p, (end || 1) + unit);
							start = ( (end || 1) / e.cur() ) * start;
							teabo.style( this, p, start + unit);
						}

						// If a +=/-= token was provided, we're doing a relative animation
						if ( parts[1] ) {
							end = ( (parts[ 1 ] === "-=" ? -1 : 1) * end ) + start;
						}

						e.custom( start, end, unit );

					} else {
						e.custom( start, val, "" );
					}
				}
			}

			// For JS strict compliance
			return true;
		}

		return optall.queue === false ?
				this.each( doAnimation ) :
					this.queue( optall.queue, doAnimation );
	},

	stop: function( type, clearQueue, gotoEnd ) {
		if ( typeof type !== "string" ) {
			gotoEnd = clearQueue;
			clearQueue = type;
			type = undefined;
		}
		if ( clearQueue && type !== false ) {
			this.queue( type || "fx", [] );
		}

		return this.each(function() {
			var index,
			hadTimers = false,
			timers = teabo.timers,
			data = teabo._data( this );

			// clear marker counters if we know they won't be
			if ( !gotoEnd ) {
				teabo._unmark( true, this );
			}

			function stopQueue( elem, data, index ) {
				var hooks = data[ index ];
				teabo.removeData( elem, index, true );
				hooks.stop( gotoEnd );
			}

			if ( type == null ) {
				for ( index in data ) {
					if ( data[ index ] && data[ index ].stop && index.indexOf(".run") === index.length - 4 ) {
						stopQueue( this, data, index );
					}
				}
			} else if ( data[ index = type + ".run" ] && data[ index ].stop ){
				stopQueue( this, data, index );
			}

			for ( index = timers.length; index--; ) {
				if ( timers[ index ].elem === this && (type == null || timers[ index ].queue === type) ) {
					if ( gotoEnd ) {

						// force the next step to be the last
						timers[ index ]( true );
					} else {
						timers[ index ].saveState();
					}
					hadTimers = true;
					timers.splice( index, 1 );
				}
			}

			// start the next in the queue if the last step wasn't forced
			// timers currently will call their complete callbacks, which will dequeue
			// but only if they were gotoEnd
			if ( !( gotoEnd && hadTimers ) ) {
				teabo.dequeue( this, type );
			}
		});

	}

});

//Animations created synchronously will run synchronously
function createFxNow() {
	setTimeout( clearFxNow, 0 );
	return ( fxNow = teabo.now() );
}

function clearFxNow() {
	fxNow = undefined;
}

//Generate parameters to create a standard animation
function genFx( type, num ) {
	var obj = {};

	teabo.each( fxAttrs.concat.apply([], fxAttrs.slice( 0, num )), function() {
		obj[ this ] = type;
	});

	return obj;
}
//Generate shortcuts for custom animations
teabo.each({
	slideDown: genFx( "show", 1 ),
	slideUp: genFx( "hide", 1 ),
	slideToggle: genFx( "toggle", 1 ),
	fadeIn: { opacity: "show" },
	fadeOut: { opacity: "hide" },
	fadeToggle: { opacity: "toggle" }
}, function( name, props ) {
	teabo.fn[ name ] = function( speed, easing, callback ) {
		return this.animate( props, speed, easing, callback );
	};
});
teabo.extend({
	speed: function( speed, easing, fn ) {
		var opt = speed && typeof speed === "object" ? teabo.extend( {}, speed ) : {
			complete: fn || !fn && easing ||
			teabo.isFunction( speed ) && speed,
			duration: speed,
			easing: fn && easing || easing && !teabo.isFunction( easing ) && easing
		};

		opt.duration = teabo.fx.off ? 0 : typeof opt.duration === "number" ? opt.duration :
			opt.duration in teabo.fx.speeds ? teabo.fx.speeds[ opt.duration ] : teabo.fx.speeds._default;

			// normalize opt.queue - true/undefined/null -> "fx"
			if ( opt.queue == null || opt.queue === true ) {
				opt.queue = "fx";
			}

			// Queueing
			opt.old = opt.complete;

			opt.complete = function( noUnmark ) {
				if ( teabo.isFunction( opt.old ) ) {
					opt.old.call( this );
				}

				if ( opt.queue ) {
					teabo.dequeue( this, opt.queue );
				} else if ( noUnmark !== false ) {
					teabo._unmark( this );
				}
			};

			return opt;
	},

	easing: {
		linear: function( p ) {
			return p;
		},
		swing: function( p ) {
			return ( -Math.cos( p*Math.PI ) / 2 ) + 0.5;
		}
	},

	timers: [],

	fx: function( elem, options, prop ) {
		this.options = options;
		this.elem = elem;
		this.prop = prop;

		options.orig = options.orig || {};
	}

});
teabo.fx.prototype = {
		// Simple function for setting a style value
		update: function() {
			if ( this.options.step ) {
				this.options.step.call( this.elem, this.now, this );
			}

			( teabo.fx.step[ this.prop ] || teabo.fx.step._default )( this );
		},

		// Get the current size
		cur: function() {
			if ( this.elem[ this.prop ] != null && (!this.elem.style || this.elem.style[ this.prop ] == null) ) {
				return this.elem[ this.prop ];
			}

			var parsed,
				r = teabo.css( this.elem, this.prop );
			// Empty strings, null, undefined and "auto" are converted to 0,
			// complex values such as "rotate(1rad)" are returned as is,
			// simple values such as "10px" are parsed to Float.
			return isNaN( parsed = parseFloat( r ) ) ? !r || r === "auto" ? 0 : r : parsed;
		},

		// Start an animation from one number to another
		custom: function( from, to, unit ) {
			var self = this,
				fx = teabo.fx;

			this.startTime = fxNow || createFxNow();
			this.end = to;
			this.now = this.start = from;
			this.pos = this.state = 0;
			this.unit = unit || this.unit || ( teabo.cssNumber[ this.prop ] ? "" : "px" );

			function t( gotoEnd ) {
				return self.step( gotoEnd );
			}

			t.queue = this.options.queue;
			t.elem = this.elem;
			t.saveState = function() {
				if ( teabo._data( self.elem, "fxshow" + self.prop ) === undefined ) {
					if ( self.options.hide ) {
						teabo._data( self.elem, "fxshow" + self.prop, self.start );
					} else if ( self.options.show ) {
						teabo._data( self.elem, "fxshow" + self.prop, self.end );
					}
				}
			};

			if ( t() && teabo.timers.push(t) && !timerId ) {
				timerId = setInterval( fx.tick, fx.interval );
			}
		},

		// Simple 'show' function
		show: function() {
			var dataShow = teabo._data( this.elem, "fxshow" + this.prop );

			// Remember where we started, so that we can go back to it later
			this.options.orig[ this.prop ] = dataShow || teabo.style( this.elem, this.prop );
			this.options.show = true;

			// Begin the animation
			// Make sure that we start at a small width/height to avoid any flash of content
			if ( dataShow !== undefined ) {
				// This show is picking up where a previous hide or show left off
				this.custom( this.cur(), dataShow );
			} else {
				this.custom( this.prop === "width" || this.prop === "height" ? 1 : 0, this.cur() );
			}

			// Start by showing the element
			teabo( this.elem ).show();
		},

		// Simple 'hide' function
		hide: function() {
			// Remember where we started, so that we can go back to it later
			this.options.orig[ this.prop ] = teabo._data( this.elem, "fxshow" + this.prop ) || teabo.style( this.elem, this.prop );
			this.options.hide = true;

			// Begin the animation
			this.custom( this.cur(), 0 );
		},

		// Each step of an animation
		step: function( gotoEnd ) {
			var p, n, complete,
				t = fxNow || createFxNow(),
				done = true,
				elem = this.elem,
				options = this.options;

			if ( gotoEnd || t >= options.duration + this.startTime ) {
				this.now = this.end;
				this.pos = this.state = 1;
				this.update();

				options.animatedProperties[ this.prop ] = true;

				for ( p in options.animatedProperties ) {
					if ( options.animatedProperties[ p ] !== true ) {
						done = false;
					}
				}

				if ( done ) {
					// Reset the overflow
					if ( options.overflow != null && !teabo.support.shrinkWrapBlocks ) {

						teabo.each( [ "", "X", "Y" ], function( index, value ) {
							elem.style[ "overflow" + value ] = options.overflow[ index ];
						});
					}

					// Hide the element if the "hide" operation was done
					if ( options.hide ) {
						teabo( elem ).hide();
					}

					// Reset the properties, if the item has been hidden or shown
					if ( options.hide || options.show ) {
						for ( p in options.animatedProperties ) {
							teabo.style( elem, p, options.orig[ p ] );
							teabo.removeData( elem, "fxshow" + p, true );
							// Toggle data is no longer needed
							teabo.removeData( elem, "toggle" + p, true );
						}
					}

					// Execute the complete function
					// in the event that the complete function throws an exception
					// we must ensure it won't be called twice. #5684

					complete = options.complete;
					if ( complete ) {

						options.complete = false;
						complete.call( elem );
					}
				}

				return false;

			} else {
				// classical easing cannot be used with an Infinity duration
				if ( options.duration == Infinity ) {
					this.now = t;
				} else {
					n = t - this.startTime;
					this.state = n / options.duration;

					// Perform the easing function, defaults to swing
					this.pos = teabo.easing[ options.animatedProperties[this.prop] ]( this.state, n, 0, 1, options.duration );
					this.now = this.start + ( (this.end - this.start) * this.pos );
				}
				// Perform the next step of the animation
				this.update();
			}

			return true;
		}
	};

	teabo.extend( teabo.fx, {
		tick: function() {
			var timer,
				timers = teabo.timers,
				i = 0;

			for ( ; i < timers.length; i++ ) {
				timer = timers[ i ];
				// Checks the timer has not already been removed
				if ( !timer() && timers[ i ] === timer ) {
					timers.splice( i--, 1 );
				}
			}

			if ( !timers.length ) {
				teabo.fx.stop();
			}
		},

		interval: 13,

		stop: function() {
			clearInterval( timerId );
			timerId = null;
		},

		speeds: {
			slow: 600,
			fast: 200,
			// Default speed
			_default: 400
		},

		step: {
			opacity: function( fx ) {
				teabo.style( fx.elem, "opacity", fx.now );
			},

			_default: function( fx ) {
				if ( fx.elem.style && fx.elem.style[ fx.prop ] != null ) {
					fx.elem.style[ fx.prop ] = fx.now + fx.unit;
				} else {
					fx.elem[ fx.prop ] = fx.now;
				}
			}
		}
	});

	// Ensure props that can't be negative don't go there on undershoot easing
	teabo.each( fxAttrs.concat.apply( [], fxAttrs ), function( i, prop ) {
		// exclude marginTop, marginLeft, marginBottom and marginRight from this list
		if ( prop.indexOf( "margin" ) ) {
			teabo.fx.step[ prop ] = function( fx ) {
				teabo.style( fx.elem, prop, Math.max(0, fx.now) + fx.unit );
			};
		}
	});

	if ( teabo.expr && teabo.expr.filters ) {
		teabo.expr.filters.animated = function( elem ) {
			return teabo.grep(teabo.timers, function( fn ) {
				return elem === fn.elem;
			}).length;
		};
	}

	// Try to restore the default display value of an element
	function defaultDisplay( nodeName ) {

		if ( !elemdisplay[ nodeName ] ) {

			var body = document.body,
				elem = teabo( "<" + nodeName + ">" ).appendTo( body ),
				display = elem.css( "display" );
			elem.remove();

			// If the simple way fails,
			// get element's real default display by attaching it to a temp iframe
			if ( display === "none" || display === "" ) {
				// No iframe to use yet, so create it
				if ( !iframe ) {
					iframe = document.createElement( "iframe" );
					iframe.frameBorder = iframe.width = iframe.height = 0;
				}

				body.appendChild( iframe );

				// Create a cacheable copy of the iframe document on first call.
				// IE and Opera will allow us to reuse the iframeDoc without re-writing the fake HTML
				// document to it; WebKit & Firefox won't allow reusing the iframe document.
				if ( !iframeDoc || !iframe.createElement ) {
					iframeDoc = ( iframe.contentWindow || iframe.contentDocument ).document;
					iframeDoc.write( ( teabo.support.boxModel ? "<!doctype html>" : "" ) + "<html><body>" );
					iframeDoc.close();
				}

				elem = iframeDoc.createElement( nodeName );

				iframeDoc.body.appendChild( elem );

				display = teabo.css( elem, "display" );
				body.removeChild( iframe );
			}

			// Store the correct default display
			elemdisplay[ nodeName ] = display;
		}

		return elemdisplay[ nodeName ];
	}
	
	function createSafeFragment( document ) {
		var list = nodeNames.split( "|" ),
		safeFrag = document.createDocumentFragment();

		if ( safeFrag.createElement ) {
			while ( list.length ) {
				safeFrag.createElement(
					list.pop()
				);
			}
		}
		return safeFrag;
	}
	var nodeNames = "abbr|article|aside|audio|bdi|canvas|data|datalist|details|figcaption|figure|footer|" +
			"header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",
		rinlinejQuery = / teabo\d+="(?:\d+|null)"/g,
		rleadingWhitespace = /^\s+/,
		rxhtmlTag = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/ig,
		rtagName = /<([\w:]+)/,
		rtbody = /<tbody/i,
		rhtml = /<|&#?\w+;/,
		rnoInnerhtml = /<(?:script|style)/i,
		rnocache = /<(?:script|object|embed|option|style)/i,
		rnoshimcache = new RegExp("<(?:" + nodeNames + ")[\\s/>]", "i"),
		// checked="checked" or checked
		rchecked = /checked\s*(?:[^=]|=\s*.checked.)/i,
		rscriptType = /\/(java|ecma)script/i,
		rcleanScript = /^\s*<!(?:\[CDATA\[|\-\-)/,
		wrapMap = {
			option: [ 1, "<select multiple='multiple'>", "</select>" ],
			legend: [ 1, "<fieldset>", "</fieldset>" ],
			thead: [ 1, "<table>", "</table>" ],
			tr: [ 2, "<table><tbody>", "</tbody></table>" ],
			td: [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ],
			col: [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ],
			area: [ 1, "<map>", "</map>" ],
			_default: [ 0, "", "" ]
		},
		safeFragment = createSafeFragment( document );

	wrapMap.optgroup = wrapMap.option;
	wrapMap.tbody = wrapMap.tfoot = wrapMap.colgroup = wrapMap.caption = wrapMap.thead;
	wrapMap.th = wrapMap.td;

	// IE can't serialize <link> and <script> tags normally
	if ( !teabo.support.htmlSerialize ) {
		wrapMap._default = [ 1, "div<div>", "</div>" ];
	}

	teabo.fn.extend({
		text: function( value ) {
			return teabo.access( this, function( value ) {
				return value === undefined ?
					teabo.text( this ) :
					this.empty().append( ( this[0] && this[0].ownerDocument || document ).createTextNode( value ) );
			}, null, value, arguments.length );
		},

		wrapAll: function( html ) {
			if ( teabo.isFunction( html ) ) {
				return this.each(function(i) {
					teabo(this).wrapAll( html.call(this, i) );
				});
			}

			if ( this[0] ) {
				// The elements to wrap the target around
				var wrap = teabo( html, this[0].ownerDocument ).eq(0).clone(true);

				if ( this[0].parentNode ) {
					wrap.insertBefore( this[0] );
				}

				wrap.map(function() {
					var elem = this;

					while ( elem.firstChild && elem.firstChild.nodeType === 1 ) {
						elem = elem.firstChild;
					}

					return elem;
				}).append( this );
			}

			return this;
		},

		wrapInner: function( html ) {
			if ( teabo.isFunction( html ) ) {
				return this.each(function(i) {
					teabo(this).wrapInner( html.call(this, i) );
				});
			}

			return this.each(function() {
				var self = teabo( this ),
					contents = self.contents();

				if ( contents.length ) {
					contents.wrapAll( html );

				} else {
					self.append( html );
				}
			});
		},

		wrap: function( html ) {
			var isFunction = teabo.isFunction( html );

			return this.each(function(i) {
				teabo( this ).wrapAll( isFunction ? html.call(this, i) : html );
			});
		},

		unwrap: function() {
			return this.parent().each(function() {
				if ( !teabo.nodeName( this, "body" ) ) {
					teabo( this ).replaceWith( this.childNodes );
				}
			}).end();
		},

		append: function() {
			return this.domManip(arguments, true, function( elem ) {
				if ( this.nodeType === 1 ) {
					this.appendChild( elem );
				}
			});
		},

		prepend: function() {
			return this.domManip(arguments, true, function( elem ) {
				if ( this.nodeType === 1 ) {
					this.insertBefore( elem, this.firstChild );
				}
			});
		},

		before: function() {
			if ( this[0] && this[0].parentNode ) {
				return this.domManip(arguments, false, function( elem ) {
					this.parentNode.insertBefore( elem, this );
				});
			} else if ( arguments.length ) {
				var set = teabo.clean( arguments );
				set.push.apply( set, this.toArray() );
				return this.pushStack( set, "before", arguments );
			}
		},

		after: function() {
			if ( this[0] && this[0].parentNode ) {
				return this.domManip(arguments, false, function( elem ) {
					this.parentNode.insertBefore( elem, this.nextSibling );
				});
			} else if ( arguments.length ) {
				var set = this.pushStack( this, "after", arguments );
				set.push.apply( set, teabo.clean(arguments) );
				return set;
			}
		},

		// keepData is for internal use only--do not document
		remove: function( selector, keepData ) {
			for ( var i = 0, elem; (elem = this[i]) != null; i++ ) {
				if ( !selector || teabo.filter( selector, [ elem ] ).length ) {
					if ( !keepData && elem.nodeType === 1 ) {
						teabo.cleanData( elem.getElementsByTagName("*") );
						teabo.cleanData( [ elem ] );
					}

					if ( elem.parentNode ) {
						elem.parentNode.removeChild( elem );
					}
				}
			}

			return this;
		},

		empty: function() {
			for ( var i = 0, elem; (elem = this[i]) != null; i++ ) {
				// Remove element nodes and prevent memory leaks
				if ( elem.nodeType === 1 ) {
					teabo.cleanData( elem.getElementsByTagName("*") );
				}

				// Remove any remaining nodes
				while ( elem.firstChild ) {
					elem.removeChild( elem.firstChild );
				}
			}

			return this;
		},

		clone: function( dataAndEvents, deepDataAndEvents ) {
			dataAndEvents = dataAndEvents == null ? false : dataAndEvents;
			deepDataAndEvents = deepDataAndEvents == null ? dataAndEvents : deepDataAndEvents;

			return this.map( function () {
				return teabo.clone( this, dataAndEvents, deepDataAndEvents );
			});
		},

		html: function( value ) {
			return teabo.access( this, function( value ) {
				var elem = this[0] || {},
					i = 0,
					l = this.length;

				if ( value === undefined ) {
					return elem.nodeType === 1 ?
						elem.innerHTML.replace( rinlinejQuery, "" ) :
						null;
				}


				if ( typeof value === "string" && !rnoInnerhtml.test( value ) &&
					( teabo.support.leadingWhitespace || !rleadingWhitespace.test( value ) ) &&
					!wrapMap[ ( rtagName.exec( value ) || ["", ""] )[1].toLowerCase() ] ) {

					value = value.replace( rxhtmlTag, "<$1></$2>" );

					try {
						for (; i < l; i++ ) {
							// Remove element nodes and prevent memory leaks
							elem = this[i] || {};
							if ( elem.nodeType === 1 ) {
								teabo.cleanData( elem.getElementsByTagName( "*" ) );
								elem.innerHTML = value;
							}
						}

						elem = 0;

					// If using innerHTML throws an exception, use the fallback method
					} catch(e) {}
				}

				if ( elem ) {
					this.empty().append( value );
				}
			}, null, value, arguments.length );
		},

		replaceWith: function( value ) {
			if ( this[0] && this[0].parentNode ) {
				// Make sure that the elements are removed from the DOM before they are inserted
				// this can help fix replacing a parent with child elements
				if ( teabo.isFunction( value ) ) {
					return this.each(function(i) {
						var self = teabo(this), old = self.html();
						self.replaceWith( value.call( this, i, old ) );
					});
				}

				if ( typeof value !== "string" ) {
					value = teabo( value ).detach();
				}

				return this.each(function() {
					var next = this.nextSibling,
						parent = this.parentNode;

					teabo( this ).remove();

					if ( next ) {
						teabo(next).before( value );
					} else {
						teabo(parent).append( value );
					}
				});
			} else {
				return this.length ?
					this.pushStack( teabo(teabo.isFunction(value) ? value() : value), "replaceWith", value ) :
					this;
			}
		},

		detach: function( selector ) {
			return this.remove( selector, true );
		},

		domManip: function( args, table, callback ) {
			var results, first, fragment, parent,
				value = args[0],
				scripts = [];

			// We can't cloneNode fragments that contain checked, in WebKit
			if ( !teabo.support.checkClone && arguments.length === 3 && typeof value === "string" && rchecked.test( value ) ) {
				return this.each(function() {
					teabo(this).domManip( args, table, callback, true );
				});
			}

			if ( teabo.isFunction(value) ) {
				return this.each(function(i) {
					var self = teabo(this);
					args[0] = value.call(this, i, table ? self.html() : undefined);
					self.domManip( args, table, callback );
				});
			}

			if ( this[0] ) {
				parent = value && value.parentNode;

				// If we're in a fragment, just use that instead of building a new one
				if ( teabo.support.parentNode && parent && parent.nodeType === 11 && parent.childNodes.length === this.length ) {
					results = { fragment: parent };

				} else {
					results = teabo.buildFragment( args, this, scripts );
				}

				fragment = results.fragment;

				if ( fragment.childNodes.length === 1 ) {
					first = fragment = fragment.firstChild;
				} else {
					first = fragment.firstChild;
				}

				if ( first ) {
					table = table && teabo.nodeName( first, "tr" );

					for ( var i = 0, l = this.length, lastIndex = l - 1; i < l; i++ ) {
						callback.call(
							table ?
								root(this[i], first) :
								this[i],
							// Make sure that we do not leak memory by inadvertently discarding
							// the original fragment (which might have attached data) instead of
							// using it; in addition, use the original fragment object for the last
							// item instead of first because it can end up being emptied incorrectly
							// in certain situations (Bug #8070).
							// Fragments from the fragment cache must always be cloned and never used
							// in place.
							results.cacheable || ( l > 1 && i < lastIndex ) ?
								teabo.clone( fragment, true, true ) :
								fragment
						);
					}
				}

				if ( scripts.length ) {
					teabo.each( scripts, function( i, elem ) {
						if ( elem.src ) {
							teabo.ajax({
								type: "GET",
								global: false,
								url: elem.src,
								async: false,
								dataType: "script"
							});
						} else {
							teabo.globalEval( ( elem.text || elem.textContent || elem.innerHTML || "" ).replace( rcleanScript, "/*$0*/" ) );
						}

						if ( elem.parentNode ) {
							elem.parentNode.removeChild( elem );
						}
					});
				}
			}

			return this;
		}
	});

	function root( elem, cur ) {
		return teabo.nodeName(elem, "table") ?
			(elem.getElementsByTagName("tbody")[0] ||
			elem.appendChild(elem.ownerDocument.createElement("tbody"))) :
			elem;
	}

	function cloneCopyEvent( src, dest ) {

		if ( dest.nodeType !== 1 || !teabo.hasData( src ) ) {
			return;
		}

		var type, i, l,
			oldData = teabo._data( src ),
			curData = teabo._data( dest, oldData ),
			events = oldData.events;

		if ( events ) {
			delete curData.handle;
			curData.events = {};

			for ( type in events ) {
				for ( i = 0, l = events[ type ].length; i < l; i++ ) {
					teabo.event.add( dest, type, events[ type ][ i ] );
				}
			}
		}

		// make the cloned public data object a copy from the original
		if ( curData.data ) {
			curData.data = teabo.extend( {}, curData.data );
		}
	}

	function cloneFixAttributes( src, dest ) {
		var nodeName;

		// We do not need to do anything for non-Elements
		if ( dest.nodeType !== 1 ) {
			return;
		}

		// clearAttributes removes the attributes, which we don't want,
		// but also removes the attachEvent events, which we *do* want
		if ( dest.clearAttributes ) {
			dest.clearAttributes();
		}

		// mergeAttributes, in contrast, only merges back on the
		// original attributes, not the events
		if ( dest.mergeAttributes ) {
			dest.mergeAttributes( src );
		}

		nodeName = dest.nodeName.toLowerCase();

		// IE6-8 fail to clone children inside object elements that use
		// the proprietary classid attribute value (rather than the type
		// attribute) to identify the type of content to display
		if ( nodeName === "object" ) {
			dest.outerHTML = src.outerHTML;

		} else if ( nodeName === "input" && (src.type === "checkbox" || src.type === "radio") ) {
			// IE6-8 fails to persist the checked state of a cloned checkbox
			// or radio button. Worse, IE6-7 fail to give the cloned element
			// a checked appearance if the defaultChecked value isn't also set
			if ( src.checked ) {
				dest.defaultChecked = dest.checked = src.checked;
			}

			// IE6-7 get confused and end up setting the value of a cloned
			// checkbox/radio button to an empty string instead of "on"
			if ( dest.value !== src.value ) {
				dest.value = src.value;
			}

		// IE6-8 fails to return the selected option to the default selected
		// state when cloning options
		} else if ( nodeName === "option" ) {
			dest.selected = src.defaultSelected;

		// IE6-8 fails to set the defaultValue to the correct value when
		// cloning other types of input fields
		} else if ( nodeName === "input" || nodeName === "textarea" ) {
			dest.defaultValue = src.defaultValue;

		// IE blanks contents when cloning scripts
		} else if ( nodeName === "script" && dest.text !== src.text ) {
			dest.text = src.text;
		}

		// Event data gets referenced instead of copied if the expando
		// gets copied too
		dest.removeAttribute( teabo.expando );

		// Clear flags for bubbling special change/submit events, they must
		// be reattached when the newly cloned events are first activated
		dest.removeAttribute( "_submit_attached" );
		dest.removeAttribute( "_change_attached" );
	}

	teabo.buildFragment = function( args, nodes, scripts ) {
		var fragment, cacheable, cacheresults, doc,
		first = args[ 0 ];

		// nodes may contain either an explicit document object,
		// a teabo collection or context object.
		// If nodes[0] contains a valid object to assign to doc
		if ( nodes && nodes[0] ) {
			doc = nodes[0].ownerDocument || nodes[0];
		}

		// Ensure that an attr object doesn't incorrectly stand in as a document object
		// Chrome and Firefox seem to allow this to occur and will throw exception
		// Fixes #8950
		if ( !doc.createDocumentFragment ) {
			doc = document;
		}

		// Only cache "small" (1/2 KB) HTML strings that are associated with the main document
		// Cloning options loses the selected state, so don't cache them
		// IE 6 doesn't like it when you put <object> or <embed> elements in a fragment
		// Also, WebKit does not clone 'checked' attributes on cloneNode, so don't cache
		// Lastly, IE6,7,8 will not correctly reuse cached fragments that were created from unknown elems #10501
		if ( args.length === 1 && typeof first === "string" && first.length < 512 && doc === document &&
			first.charAt(0) === "<" && !rnocache.test( first ) &&
			(teabo.support.checkClone || !rchecked.test( first )) &&
			(teabo.support.html5Clone || !rnoshimcache.test( first )) ) {

			cacheable = true;

			cacheresults = teabo.fragments[ first ];
			if ( cacheresults && cacheresults !== 1 ) {
				fragment = cacheresults;
			}
		}

		if ( !fragment ) {
			fragment = doc.createDocumentFragment();
			teabo.clean( args, doc, fragment, scripts );
		}

		if ( cacheable ) {
			teabo.fragments[ first ] = cacheresults ? fragment : 1;
		}

		return { fragment: fragment, cacheable: cacheable };
	};

	teabo.fragments = {};

	teabo.each({
		appendTo: "append",
		prependTo: "prepend",
		insertBefore: "before",
		insertAfter: "after",
		replaceAll: "replaceWith"
	}, function( name, original ) {
		teabo.fn[ name ] = function( selector ) {
			var ret = [],
				insert = teabo( selector ),
				parent = this.length === 1 && this[0].parentNode;

			if ( parent && parent.nodeType === 11 && parent.childNodes.length === 1 && insert.length === 1 ) {
				insert[ original ]( this[0] );
				return this;

			} else {
				for ( var i = 0, l = insert.length; i < l; i++ ) {
					var elems = ( i > 0 ? this.clone(true) : this ).get();
					teabo( insert[i] )[ original ]( elems );
					ret = ret.concat( elems );
				}

				return this.pushStack( ret, name, insert.selector );
			}
		};
	});

	function getAll( elem ) {
		if ( typeof elem.getElementsByTagName !== "undefined" ) {
			return elem.getElementsByTagName( "*" );

		} else if ( typeof elem.querySelectorAll !== "undefined" ) {
			return elem.querySelectorAll( "*" );

		} else {
			return [];
		}
	}

	// Used in clean, fixes the defaultChecked property
	function fixDefaultChecked( elem ) {
		if ( elem.type === "checkbox" || elem.type === "radio" ) {
			elem.defaultChecked = elem.checked;
		}
	}
	// Finds all inputs and passes them to fixDefaultChecked
	function findInputs( elem ) {
		var nodeName = ( elem.nodeName || "" ).toLowerCase();
		if ( nodeName === "input" ) {
			fixDefaultChecked( elem );
		// Skip scripts, get other children
		} else if ( nodeName !== "script" && typeof elem.getElementsByTagName !== "undefined" ) {
			teabo.grep( elem.getElementsByTagName("input"), fixDefaultChecked );
		}
	}

	// Derived From: http://www.iecss.com/shimprove/javascript/shimprove.1-0-1.js
	function shimCloneNode( elem ) {
		var div = document.createElement( "div" );
		safeFragment.appendChild( div );

		div.innerHTML = elem.outerHTML;
		return div.firstChild;
	}

	teabo.extend({
		clone: function( elem, dataAndEvents, deepDataAndEvents ) {
			var srcElements,
				destElements,
				i,
				// IE<=8 does not properly clone detached, unknown element nodes
				clone = teabo.support.html5Clone || teabo.isXMLDoc(elem) || !rnoshimcache.test( "<" + elem.nodeName + ">" ) ?
					elem.cloneNode( true ) :
					shimCloneNode( elem );

			if ( (!teabo.support.noCloneEvent || !teabo.support.noCloneChecked) &&
					(elem.nodeType === 1 || elem.nodeType === 11) && !teabo.isXMLDoc(elem) ) {
				// IE copies events bound via attachEvent when using cloneNode.
				// Calling detachEvent on the clone will also remove the events
				// from the original. In order to get around this, we use some
				// proprietary methods to clear the events. Thanks to MooTools
				// guys for this hotness.

				cloneFixAttributes( elem, clone );

				// Using Sizzle here is crazy slow, so we use getElementsByTagName instead
				srcElements = getAll( elem );
				destElements = getAll( clone );

				// Weird iteration because IE will replace the length property
				// with an element if you are cloning the body and one of the
				// elements on the page has a name or id of "length"
				for ( i = 0; srcElements[i]; ++i ) {
					// Ensure that the destination node is not null; Fixes #9587
					if ( destElements[i] ) {
						cloneFixAttributes( srcElements[i], destElements[i] );
					}
				}
			}

			// Copy the events from the original to the clone
			if ( dataAndEvents ) {
				cloneCopyEvent( elem, clone );

				if ( deepDataAndEvents ) {
					srcElements = getAll( elem );
					destElements = getAll( clone );

					for ( i = 0; srcElements[i]; ++i ) {
						cloneCopyEvent( srcElements[i], destElements[i] );
					}
				}
			}

			srcElements = destElements = null;

			// Return the cloned set
			return clone;
		},

		clean: function( elems, context, fragment, scripts ) {
			var checkScriptType, script, j,
					ret = [];

			context = context || document;

			// !context.createElement fails in IE with an error but returns typeof 'object'
			if ( typeof context.createElement === "undefined" ) {
				context = context.ownerDocument || context[0] && context[0].ownerDocument || document;
			}

			for ( var i = 0, elem; (elem = elems[i]) != null; i++ ) {
				if ( typeof elem === "number" ) {
					elem += "";
				}

				if ( !elem ) {
					continue;
				}

				// Convert html string into DOM nodes
				if ( typeof elem === "string" ) {
					if ( !rhtml.test( elem ) ) {
						elem = context.createTextNode( elem );
					} else {
						// Fix "XHTML"-style tags in all browsers
						elem = elem.replace(rxhtmlTag, "<$1></$2>");

						// Trim whitespace, otherwise indexOf won't work as expected
						var tag = ( rtagName.exec( elem ) || ["", ""] )[1].toLowerCase(),
							wrap = wrapMap[ tag ] || wrapMap._default,
							depth = wrap[0],
							div = context.createElement("div"),
							safeChildNodes = safeFragment.childNodes,
							remove;

						// Append wrapper element to unknown element safe doc fragment
						if ( context === document ) {
							// Use the fragment we've already created for this document
							safeFragment.appendChild( div );
						} else {
							// Use a fragment created with the owner document
							createSafeFragment( context ).appendChild( div );
						}

						// Go to html and back, then peel off extra wrappers
						div.innerHTML = wrap[1] + elem + wrap[2];

						// Move to the right depth
						while ( depth-- ) {
							div = div.lastChild;
						}

						// Remove IE's autoinserted <tbody> from table fragments
						if ( !teabo.support.tbody ) {

							// String was a <table>, *may* have spurious <tbody>
							var hasBody = rtbody.test(elem),
								tbody = tag === "table" && !hasBody ?
									div.firstChild && div.firstChild.childNodes :

									// String was a bare <thead> or <tfoot>
									wrap[1] === "<table>" && !hasBody ?
										div.childNodes :
										[];

							for ( j = tbody.length - 1; j >= 0 ; --j ) {
								if ( teabo.nodeName( tbody[ j ], "tbody" ) && !tbody[ j ].childNodes.length ) {
									tbody[ j ].parentNode.removeChild( tbody[ j ] );
								}
							}
						}

						// IE completely kills leading whitespace when innerHTML is used
						if ( !teabo.support.leadingWhitespace && rleadingWhitespace.test( elem ) ) {
							div.insertBefore( context.createTextNode( rleadingWhitespace.exec(elem)[0] ), div.firstChild );
						}

						elem = div.childNodes;

						// Clear elements from DocumentFragment (safeFragment or otherwise)
						// to avoid hoarding elements. Fixes #11356
						if ( div ) {
							div.parentNode.removeChild( div );

							// Guard against -1 index exceptions in FF3.6
							if ( safeChildNodes.length > 0 ) {
								remove = safeChildNodes[ safeChildNodes.length - 1 ];

								if ( remove && remove.parentNode ) {
									remove.parentNode.removeChild( remove );
								}
							}
						}
					}
				}

				// Resets defaultChecked for any radios and checkboxes
				// about to be appended to the DOM in IE 6/7 (#8060)
				var len;
				if ( !teabo.support.appendChecked ) {
					if ( elem[0] && typeof (len = elem.length) === "number" ) {
						for ( j = 0; j < len; j++ ) {
							findInputs( elem[j] );
						}
					} else {
						findInputs( elem );
					}
				}

				if ( elem.nodeType ) {
					ret.push( elem );
				} else {
					ret = teabo.merge( ret, elem );
				}
			}

			if ( fragment ) {
				checkScriptType = function( elem ) {
					return !elem.type || rscriptType.test( elem.type );
				};
				for ( i = 0; ret[i]; i++ ) {
					script = ret[i];
					if ( scripts && teabo.nodeName( script, "script" ) && (!script.type || rscriptType.test( script.type )) ) {
						scripts.push( script.parentNode ? script.parentNode.removeChild( script ) : script );

					} else {
						if ( script.nodeType === 1 ) {
							var jsTags = teabo.grep( script.getElementsByTagName( "script" ), checkScriptType );

							ret.splice.apply( ret, [i + 1, 0].concat( jsTags ) );
						}
						fragment.appendChild( script );
					}
				}
			}

			return ret;
		},

		cleanData: function( elems ) {
			var data, id,
				cache = teabo.cache,
				special = teabo.event.special,
				deleteExpando = teabo.support.deleteExpando;

			for ( var i = 0, elem; (elem = elems[i]) != null; i++ ) {
				if ( elem.nodeName && teabo.noData[elem.nodeName.toLowerCase()] ) {
					continue;
				}

				id = elem[ teabo.expando ];

				if ( id ) {
					data = cache[ id ];

					if ( data && data.events ) {
						for ( var type in data.events ) {
							if ( special[ type ] ) {
								teabo.event.remove( elem, type );

							// This is a shortcut to avoid teabo.event.remove's overhead
							} else {
								teabo.removeEvent( elem, type, data.handle );
							}
						}

						// Null the DOM reference to avoid IE6/7/8 leak (#7054)
						if ( data.handle ) {
							data.handle.elem = null;
						}
					}

					if ( deleteExpando ) {
						delete elem[ teabo.expando ];

					} else if ( elem.removeAttribute ) {
						elem.removeAttribute( teabo.expando );
					}

					delete cache[ id ];
				}
			}
		}
	});

	
var rformElems = /^(?:textarea|input|select)$/i,
rtypenamespace = /^([^\.]*)?(?:\.(.+))?$/,
rhoverHack = /(?:^|\s)hover(\.\S+)?\b/,
rkeyEvent = /^key/,
rmouseEvent = /^(?:mouse|contextmenu)|click/,
rfocusMorph = /^(?:focusinfocus|focusoutblur)$/,
rquickIs = /^(\w*)(?:#([\w\-]+))?(?:\.([\w\-]+))?$/,
quickParse = function( selector ) {
	var quick = rquickIs.exec( selector );
	if ( quick ) {
		//   0  1    2   3
		// [ _, tag, id, class ]
		quick[1] = ( quick[1] || "" ).toLowerCase();
		quick[3] = quick[3] && new RegExp( "(?:^|\\s)" + quick[3] + "(?:\\s|$)" );
	}
	return quick;
},
quickIs = function( elem, m ) {
	var attrs = elem.attributes || {};
	return (
		(!m[1] || elem.nodeName.toLowerCase() === m[1]) &&
		(!m[2] || (attrs.id || {}).value === m[2]) &&
		(!m[3] || m[3].test( (attrs[ "class" ] || {}).value ))
	);
},
hoverHack = function( events ) {
	return teabo.event.special.hover ? events : events.replace( rhoverHack, "mouseenter$1 mouseleave$1" );
};

/*
* Helper functions for managing events -- not part of the public interface.
* Props to Dean Edwards' addEvent library for many of the ideas.
*/
teabo.event = {

		add: function( elem, types, handler, data, selector ) {

			var elemData, eventHandle, events,
			t, tns, type, namespaces, handleObj,
			handleObjIn, quick, handlers, special;

			// Don't attach events to noData or text/comment nodes (allow plain objects tho)
			if ( elem.nodeType === 3 || elem.nodeType === 8 || !types || !handler || !(elemData = teabo._data( elem )) ) {
				return;
			}

			// Caller can pass in an object of custom data in lieu of the handler
			if ( handler.handler ) {
				handleObjIn = handler;
				handler = handleObjIn.handler;
				selector = handleObjIn.selector;
			}

			// Make sure that the handler has a unique ID, used to find/remove it later
			if ( !handler.guid ) {
				handler.guid = teabo.guid++;
			}

			// Init the element's event structure and main handler, if this is the first
			events = elemData.events;
			if ( !events ) {
				elemData.events = events = {};
			}
			eventHandle = elemData.handle;
			if ( !eventHandle ) {
				elemData.handle = eventHandle = function( e ) {
					// Discard the second event of a teabo.event.trigger() and
					// when an event is called after a page has unloaded
					return typeof teabo !== "undefined" && (!e || teabo.event.triggered !== e.type) ?
							teabo.event.dispatch.apply( eventHandle.elem, arguments ) :
								undefined;
				};
				// Add elem as a property of the handle fn to prevent a memory leak with IE non-native events
				eventHandle.elem = elem;
			}

			// Handle multiple events separated by a space
			// teabo(...).bind("mouseover mouseout", fn);
			types = teabo.trim( hoverHack(types) ).split( " " );
			for ( t = 0; t < types.length; t++ ) {

				tns = rtypenamespace.exec( types[t] ) || [];
				type = tns[1];
				namespaces = ( tns[2] || "" ).split( "." ).sort();

				// If event changes its type, use the special event handlers for the changed type
				special = teabo.event.special[ type ] || {};

				// If selector defined, determine special event api type, otherwise given type
				type = ( selector ? special.delegateType : special.bindType ) || type;

				// Update special based on newly reset type
				special = teabo.event.special[ type ] || {};

				// handleObj is passed to all event handlers
				handleObj = teabo.extend({
					type: type,
					origType: tns[1],
					data: data,
					handler: handler,
					guid: handler.guid,
					selector: selector,
					quick: selector && quickParse( selector ),
					namespace: namespaces.join(".")
				}, handleObjIn );

				// Init the event handler queue if we're the first
				handlers = events[ type ];
				if ( !handlers ) {
					handlers = events[ type ] = [];
					handlers.delegateCount = 0;

					// Only use addEventListener/attachEvent if the special events handler returns false
					if ( !special.setup || special.setup.call( elem, data, namespaces, eventHandle ) === false ) {
						// Bind the global event handler to the element
						if ( elem.addEventListener ) {
							elem.addEventListener( type, eventHandle, false );

						} else if ( elem.attachEvent ) {
							elem.attachEvent( "on" + type, eventHandle );
						}
					}
				}

				if ( special.add ) {
					special.add.call( elem, handleObj );

					if ( !handleObj.handler.guid ) {
						handleObj.handler.guid = handler.guid;
					}
				}

				// Add to the element's handler list, delegates in front
				if ( selector ) {
					handlers.splice( handlers.delegateCount++, 0, handleObj );
				} else {
					handlers.push( handleObj );
				}

				// Keep track of which events have ever been used, for event optimization
				teabo.event.global[ type ] = true;
			}

			// Nullify elem to prevent memory leaks in IE
			elem = null;
		},

		global: {},

//		Detach an event or set of events from an element
		remove: function( elem, types, handler, selector, mappedTypes ) {

			var elemData = teabo.hasData( elem ) && teabo._data( elem ),
			t, tns, type, origType, namespaces, origCount,
			j, events, special, handle, eventType, handleObj;

			if ( !elemData || !(events = elemData.events) ) {
				return;
			}

			// Once for each type.namespace in types; type may be omitted
			types = teabo.trim( hoverHack( types || "" ) ).split(" ");
			for ( t = 0; t < types.length; t++ ) {
				tns = rtypenamespace.exec( types[t] ) || [];
				type = origType = tns[1];
				namespaces = tns[2];

				// Unbind all events (on this namespace, if provided) for the element
				if ( !type ) {
					for ( type in events ) {
						teabo.event.remove( elem, type + types[ t ], handler, selector, true );
					}
					continue;
				}

				special = teabo.event.special[ type ] || {};
				type = ( selector? special.delegateType : special.bindType ) || type;
				eventType = events[ type ] || [];
				origCount = eventType.length;
				namespaces = namespaces ? new RegExp("(^|\\.)" + namespaces.split(".").sort().join("\\.(?:.*\\.)?") + "(\\.|$)") : null;

				// Remove matching events
				for ( j = 0; j < eventType.length; j++ ) {
					handleObj = eventType[ j ];

					if ( ( mappedTypes || origType === handleObj.origType ) &&
							( !handler || handler.guid === handleObj.guid ) &&
							( !namespaces || namespaces.test( handleObj.namespace ) ) &&
							( !selector || selector === handleObj.selector || selector === "**" && handleObj.selector ) ) {
						eventType.splice( j--, 1 );

						if ( handleObj.selector ) {
							eventType.delegateCount--;
						}
						if ( special.remove ) {
							special.remove.call( elem, handleObj );
						}
					}
				}

				// Remove generic event handler if we removed something and no more handlers exist
				// (avoids potential for endless recursion during removal of special event handlers)
				if ( eventType.length === 0 && origCount !== eventType.length ) {
					if ( !special.teardown || special.teardown.call( elem, namespaces ) === false ) {
						teabo.removeEvent( elem, type, elemData.handle );
					}

					delete events[ type ];
				}
			}

			// Remove the expando if it's no longer used
			if ( teabo.isEmptyObject( events ) ) {
				handle = elemData.handle;
				if ( handle ) {
					handle.elem = null;
				}

				// removeData also checks for emptiness and clears the expando if empty
				// so use it instead of delete
				teabo.removeData( elem, [ "events", "handle" ], true );
			}
		},

//		Events that are safe to short-circuit if no handlers are attached.
//		Native DOM events should not be added, they may have inline handlers.
		customEvent: {
			"getData": true,
			"setData": true,
			"changeData": true
		},

		trigger: function( event, data, elem, onlyHandlers ) {
			// Don't do events on text and comment nodes
			if ( elem && (elem.nodeType === 3 || elem.nodeType === 8) ) {
				return;
			}

			// Event object or event type
			var type = event.type || event,
			namespaces = [],
			cache, exclusive, i, cur, old, ontype, special, handle, eventPath, bubbleType;

			// focus/blur morphs to focusin/out; ensure we're not firing them right now
			if ( rfocusMorph.test( type + teabo.event.triggered ) ) {
				return;
			}

			if ( type.indexOf( "!" ) >= 0 ) {
				// Exclusive events trigger only for the exact event (no namespaces)
				type = type.slice(0, -1);
				exclusive = true;
			}

			if ( type.indexOf( "." ) >= 0 ) {
				// Namespaced trigger; create a regexp to match event type in handle()
				namespaces = type.split(".");
				type = namespaces.shift();
				namespaces.sort();
			}

			if ( (!elem || teabo.event.customEvent[ type ]) && !teabo.event.global[ type ] ) {
				// No teabo handlers for this event type, and it can't have inline handlers
				return;
			}

			// Caller can pass in an Event, Object, or just an event type string
			event = typeof event === "object" ?
					// teabo.Event object
					event[ teabo.expando ] ? event :
						// Object literal
						new teabo.Event( type, event ) :
							// Just the event type (string)
							new teabo.Event( type );

						event.type = type;
						event.isTrigger = true;
						event.exclusive = exclusive;
						event.namespace = namespaces.join( "." );
						event.namespace_re = event.namespace? new RegExp("(^|\\.)" + namespaces.join("\\.(?:.*\\.)?") + "(\\.|$)") : null;
						ontype = type.indexOf( ":" ) < 0 ? "on" + type : "";

						// Handle a global trigger
						if ( !elem ) {

							// TODO: Stop taunting the data cache; remove global events and always attach to document
							cache = teabo.cache;
							for ( i in cache ) {
								if ( cache[ i ].events && cache[ i ].events[ type ] ) {
									teabo.event.trigger( event, data, cache[ i ].handle.elem, true );
								}
							}
							return;
						}

						// Clean up the event in case it is being reused
						event.result = undefined;
						if ( !event.target ) {
							event.target = elem;
						}

						// Clone any incoming data and prepend the event, creating the handler arg list
						data = data != null ? teabo.makeArray( data ) : [];
						data.unshift( event );

						// Allow special events to draw outside the lines
						special = teabo.event.special[ type ] || {};
						if ( special.trigger && special.trigger.apply( elem, data ) === false ) {
							return;
						}

						// Determine event propagation path in advance, per W3C events spec (#9951)
						// Bubble up to document, then to window; watch for a global ownerDocument var (#9724)
						eventPath = [[ elem, special.bindType || type ]];
						if ( !onlyHandlers && !special.noBubble && !teabo.isWindow( elem ) ) {

							bubbleType = special.delegateType || type;
							cur = rfocusMorph.test( bubbleType + type ) ? elem : elem.parentNode;
							old = null;
							for ( ; cur; cur = cur.parentNode ) {
								eventPath.push([ cur, bubbleType ]);
								old = cur;
							}

							// Only add window if we got to document (e.g., not plain obj or detached DOM)
							if ( old && old === elem.ownerDocument ) {
								eventPath.push([ old.defaultView || old.parentWindow || window, bubbleType ]);
							}
						}

						// Fire handlers on the event path
						for ( i = 0; i < eventPath.length && !event.isPropagationStopped(); i++ ) {

							cur = eventPath[i][0];
							event.type = eventPath[i][1];

							handle = ( teabo._data( cur, "events" ) || {} )[ event.type ] && teabo._data( cur, "handle" );
							if ( handle ) {
								handle.apply( cur, data );
							}
							// Note that this is a bare JS function and not a teabo handler
							handle = ontype && cur[ ontype ];
							if ( handle && teabo.acceptData( cur ) && handle.apply( cur, data ) === false ) {
								event.preventDefault();
							}
						}
						event.type = type;

						// If nobody prevented the default action, do it now
						if ( !onlyHandlers && !event.isDefaultPrevented() ) {

							if ( (!special._default || special._default.apply( elem.ownerDocument, data ) === false) &&
									!(type === "click" && teabo.nodeName( elem, "a" )) && teabo.acceptData( elem ) ) {

								// Call a native DOM method on the target with the same name name as the event.
								// Can't use an .isFunction() check here because IE6/7 fails that test.
								// Don't do default actions on window, that's where global variables be (#6170)
								// IE<9 dies on focus/blur to hidden element (#1486)
								if ( ontype && elem[ type ] && ((type !== "focus" && type !== "blur") || event.target.offsetWidth !== 0) && !teabo.isWindow( elem ) ) {

									// Don't re-trigger an onFOO event when we call its FOO() method
									old = elem[ ontype ];

									if ( old ) {
										elem[ ontype ] = null;
									}

									// Prevent re-triggering of the same event, since we already bubbled it above
									teabo.event.triggered = type;
									elem[ type ]();
									teabo.event.triggered = undefined;

									if ( old ) {
										elem[ ontype ] = old;
									}
								}
							}
						}

						return event.result;
		},

		dispatch: function( event ) {

			// Make a writable teabo.Event from the native event object
			event = teabo.event.fix( event || window.event );

			var handlers = ( (teabo._data( this, "events" ) || {} )[ event.type ] || []),
			delegateCount = handlers.delegateCount,
			args = [].slice.call( arguments, 0 ),
			run_all = !event.exclusive && !event.namespace,
			special = teabo.event.special[ event.type ] || {},
			handlerQueue = [],
			i, j, cur, jqcur, ret, selMatch, matched, matches, handleObj, sel, related;

			// Use the fix-ed teabo.Event rather than the (read-only) native event
			args[0] = event;
			event.delegateTarget = this;

			// Call the preDispatch hook for the mapped type, and let it bail if desired
			if ( special.preDispatch && special.preDispatch.call( this, event ) === false ) {
				return;
			}

			// Determine handlers that should run if there are delegated events
			// Avoid non-left-click bubbling in Firefox (#3861)
			if ( delegateCount && !(event.button && event.type === "click") ) {

				// Pregenerate a single teabo object for reuse with .is()
				jqcur = teabo(this);
				jqcur.context = this.ownerDocument || this;

				for ( cur = event.target; cur != this; cur = cur.parentNode || this ) {

					// Don't process events on disabled elements (#6911, #8165)
					if ( cur.disabled !== true ) {
						selMatch = {};
						matches = [];
						jqcur[0] = cur;
						for ( i = 0; i < delegateCount; i++ ) {
							handleObj = handlers[ i ];
							sel = handleObj.selector;

							if ( selMatch[ sel ] === undefined ) {
								selMatch[ sel ] = (
										handleObj.quick ? quickIs( cur, handleObj.quick ) : jqcur.is( sel )
								);
							}
							if ( selMatch[ sel ] ) {
								matches.push( handleObj );
							}
						}
						if ( matches.length ) {
							handlerQueue.push({ elem: cur, matches: matches });
						}
					}
				}
			}

			// Add the remaining (directly-bound) handlers
			if ( handlers.length > delegateCount ) {
				handlerQueue.push({ elem: this, matches: handlers.slice( delegateCount ) });
			}

			// Run delegates first; they may want to stop propagation beneath us
			for ( i = 0; i < handlerQueue.length && !event.isPropagationStopped(); i++ ) {
				matched = handlerQueue[ i ];
				event.currentTarget = matched.elem;

				for ( j = 0; j < matched.matches.length && !event.isImmediatePropagationStopped(); j++ ) {
					handleObj = matched.matches[ j ];

					// Triggered event must either 1) be non-exclusive and have no namespace, or
					// 2) have namespace(s) a subset or equal to those in the bound event (both can have no namespace).
					if ( run_all || (!event.namespace && !handleObj.namespace) || event.namespace_re && event.namespace_re.test( handleObj.namespace ) ) {

						event.data = handleObj.data;
						event.handleObj = handleObj;

						ret = ( (teabo.event.special[ handleObj.origType ] || {}).handle || handleObj.handler )
						.apply( matched.elem, args );

						if ( ret !== undefined ) {
							event.result = ret;
							if ( ret === false ) {
								event.preventDefault();
								event.stopPropagation();
							}
						}
					}
				}
			}

			// Call the postDispatch hook for the mapped type
			if ( special.postDispatch ) {
				special.postDispatch.call( this, event );
			}

			return event.result;
		},

//		Includes some event props shared by KeyEvent and MouseEvent
//		*** attrChange attrName relatedNode srcElement  are not normalized, non-W3C, deprecated, will be removed in 1.8 ***
		props: "attrChange attrName relatedNode srcElement altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),

		fixHooks: {},

		keyHooks: {
			props: "char charCode key keyCode".split(" "),
			filter: function( event, original ) {

				// Add which for key events
				if ( event.which == null ) {
					event.which = original.charCode != null ? original.charCode : original.keyCode;
				}

				return event;
			}
		},

		mouseHooks: {
			props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
			filter: function( event, original ) {
				var eventDoc, doc, body,
				button = original.button,
				fromElement = original.fromElement;

				// Calculate pageX/Y if missing and clientX/Y available
				if ( event.pageX == null && original.clientX != null ) {
					eventDoc = event.target.ownerDocument || document;
					doc = eventDoc.documentElement;
					body = eventDoc.body;

					event.pageX = original.clientX + ( doc && doc.scrollLeft || body && body.scrollLeft || 0 ) - ( doc && doc.clientLeft || body && body.clientLeft || 0 );
					event.pageY = original.clientY + ( doc && doc.scrollTop  || body && body.scrollTop  || 0 ) - ( doc && doc.clientTop  || body && body.clientTop  || 0 );
				}

				// Add relatedTarget, if necessary
				if ( !event.relatedTarget && fromElement ) {
					event.relatedTarget = fromElement === event.target ? original.toElement : fromElement;
				}

				// Add which for click: 1 === left; 2 === middle; 3 === right
				// Note: button is not normalized, so don't use it
				if ( !event.which && button !== undefined ) {
					event.which = ( button & 1 ? 1 : ( button & 2 ? 3 : ( button & 4 ? 2 : 0 ) ) );
				}

				return event;
			}
		},

		fix: function( event ) {
			if ( event[ teabo.expando ] ) {
				return event;
			}

			// Create a writable copy of the event object and normalize some properties
			var i, prop,
			originalEvent = event,
			fixHook = teabo.event.fixHooks[ event.type ] || {},
			copy = fixHook.props ? this.props.concat( fixHook.props ) : this.props;

			event = teabo.Event( originalEvent );

			for ( i = copy.length; i; ) {
				prop = copy[ --i ];
				event[ prop ] = originalEvent[ prop ];
			}

			// Fix target property, if necessary (#1925, IE 6/7/8 & Safari2)
			if ( !event.target ) {
				event.target = originalEvent.srcElement || document;
			}

			// Target should not be a text node (#504, Safari)
			if ( event.target.nodeType === 3 ) {
				event.target = event.target.parentNode;
			}

			// For mouse/key events; add metaKey if it's not there (#3368, IE6/7/8)
			if ( event.metaKey === undefined ) {
				event.metaKey = event.ctrlKey;
			}

			return fixHook.filter? fixHook.filter( event, originalEvent ) : event;
		},

		special: {
			ready: {
				// Make sure the ready event is setup
				setup: teabo.bindReady
			},

			load: {
				// Prevent triggered image.load events from bubbling to window.load
				noBubble: true
			},

			focus: {
				delegateType: "focusin"
			},
			blur: {
				delegateType: "focusout"
			},

			beforeunload: {
				setup: function( data, namespaces, eventHandle ) {
					// We only want to do this special case on windows
					if ( teabo.isWindow( this ) ) {
						this.onbeforeunload = eventHandle;
					}
				},

				teardown: function( namespaces, eventHandle ) {
					if ( this.onbeforeunload === eventHandle ) {
						this.onbeforeunload = null;
					}
				}
			}
		},

		simulate: function( type, elem, event, bubble ) {
			// Piggyback on a donor event to simulate a different one.
			// Fake originalEvent to avoid donor's stopPropagation, but if the
			// simulated event prevents default then we do the same on the donor.
			var e = teabo.extend(
					new teabo.Event(),
					event,
					{ type: type,
						isSimulated: true,
						originalEvent: {}
					}
			);
			if ( bubble ) {
				teabo.event.trigger( e, null, elem );
			} else {
				teabo.event.dispatch.call( elem, e );
			}
			if ( e.isDefaultPrevented() ) {
				event.preventDefault();
			}
		}
};

//Some plugins are using, but it's undocumented/deprecated and will be removed.
//The 1.7 special event interface should provide all the hooks needed now.
teabo.event.handle = teabo.event.dispatch;

teabo.removeEvent = document.removeEventListener ?
	function( elem, type, handle ) {
		if ( elem.removeEventListener ) {
			elem.removeEventListener( type, handle, false );
		}
	} :
	function( elem, type, handle ) {
		if ( elem.detachEvent ) {
			elem.detachEvent( "on" + type, handle );
		}
};

teabo.Event = function( src, props ) {
//	Allow instantiation without the 'new' keyword
	if ( !(this instanceof teabo.Event) ) {
		return new teabo.Event( src, props );
	}

//	Event object
	if ( src && src.type ) {
		this.originalEvent = src;
		this.type = src.type;

		// Events bubbling up the document may have been marked as prevented
		// by a handler lower down the tree; reflect the correct value.
		this.isDefaultPrevented = ( src.defaultPrevented || src.returnValue === false ||
				src.getPreventDefault && src.getPreventDefault() ) ? returnTrue : returnFalse;

//		Event type
	} else {
		this.type = src;
	}

//	Put explicitly provided properties onto the event object
	if ( props ) {
		teabo.extend( this, props );
	}

//	Create a timestamp if incoming event doesn't have one
	this.timeStamp = src && src.timeStamp || teabo.now();

//	Mark it as fixed
	this[ teabo.expando ] = true;
};

function returnFalse() {
return false;
}
function returnTrue() {
return true;
}

//teabo.Event is based on DOM3 Events as specified by the ECMAScript Language Binding
//http://www.w3.org/TR/2003/WD-DOM-Level-3-Events-20030331/ecma-script-binding.html
teabo.Event.prototype = {
		preventDefault: function() {
			this.isDefaultPrevented = returnTrue;

			var e = this.originalEvent;
			if ( !e ) {
				return;
			}

			// if preventDefault exists run it on the original event
			if ( e.preventDefault ) {
				e.preventDefault();

				// otherwise set the returnValue property of the original event to false (IE)
			} else {
				e.returnValue = false;
			}
		},
		stopPropagation: function() {
			this.isPropagationStopped = returnTrue;

			var e = this.originalEvent;
			if ( !e ) {
				return;
			}
			// if stopPropagation exists run it on the original event
			if ( e.stopPropagation ) {
				e.stopPropagation();
			}
			// otherwise set the cancelBubble property of the original event to true (IE)
			e.cancelBubble = true;
		},
		stopImmediatePropagation: function() {
			this.isImmediatePropagationStopped = returnTrue;
			this.stopPropagation();
		},
		isDefaultPrevented: returnFalse,
		isPropagationStopped: returnFalse,
		isImmediatePropagationStopped: returnFalse
};

//Create mouseenter/leave events using mouseover/out and event-time checks
teabo.each({
	mouseenter: "mouseover",
	mouseleave: "mouseout"
}, function( orig, fix ) {
	teabo.event.special[ orig ] = {
			delegateType: fix,
			bindType: fix,

			handle: function( event ) {
				var target = this,
				related = event.relatedTarget,
				handleObj = event.handleObj,
				selector = handleObj.selector,
				ret;

				// For mousenter/leave call the handler if related is outside the target.
				// NB: No relatedTarget if the mouse left/entered the browser window
				if ( !related || (related !== target && !teabo.contains( target, related )) ) {
					event.type = handleObj.origType;
					ret = handleObj.handler.apply( this, arguments );
					event.type = fix;
				}
				return ret;
			}
	};
});

//IE submit delegation
if ( !teabo.support.submitBubbles ) {

	teabo.event.special.submit = {
			setup: function() {
				// Only need this for delegated form submit events
				if ( teabo.nodeName( this, "form" ) ) {
					return false;
				}

				// Lazy-add a submit handler when a descendant form may potentially be submitted
				teabo.event.add( this, "click._submit keypress._submit", function( e ) {
					// Node name check avoids a VML-related crash in IE (#9807)
					var elem = e.target,
					form = teabo.nodeName( elem, "input" ) || teabo.nodeName( elem, "button" ) ? elem.form : undefined;
					if ( form && !form._submit_attached ) {
						teabo.event.add( form, "submit._submit", function( event ) {
							event._submit_bubble = true;
						});
						form._submit_attached = true;
					}
				});
				// return undefined since we don't need an event listener
			},

			postDispatch: function( event ) {
				// If form was submitted by the user, bubble the event up the tree
				if ( event._submit_bubble ) {
					delete event._submit_bubble;
					if ( this.parentNode && !event.isTrigger ) {
						teabo.event.simulate( "submit", this.parentNode, event, true );
					}
				}
			},

			teardown: function() {
				// Only need this for delegated form submit events
				if ( teabo.nodeName( this, "form" ) ) {
					return false;
				}

				// Remove delegated handlers; cleanData eventually reaps submit handlers attached above
				teabo.event.remove( this, "._submit" );
			}
	};
}

//IE change delegation and checkbox/radio fix
if ( !teabo.support.changeBubbles ) {

	teabo.event.special.change = {

			setup: function() {

				if ( rformElems.test( this.nodeName ) ) {
					// IE doesn't fire change on a check/radio until blur; trigger it on click
					// after a propertychange. Eat the blur-change in special.change.handle.
					// This still fires onchange a second time for check/radio after blur.
					if ( this.type === "checkbox" || this.type === "radio" ) {
						teabo.event.add( this, "propertychange._change", function( event ) {
							if ( event.originalEvent.propertyName === "checked" ) {
								this._just_changed = true;
							}
						});
						teabo.event.add( this, "click._change", function( event ) {
							if ( this._just_changed && !event.isTrigger ) {
								this._just_changed = false;
								teabo.event.simulate( "change", this, event, true );
							}
						});
					}
					return false;
				}
				// Delegated event; lazy-add a change handler on descendant inputs
				teabo.event.add( this, "beforeactivate._change", function( e ) {
					var elem = e.target;

					if ( rformElems.test( elem.nodeName ) && !elem._change_attached ) {
						teabo.event.add( elem, "change._change", function( event ) {
							if ( this.parentNode && !event.isSimulated && !event.isTrigger ) {
								teabo.event.simulate( "change", this.parentNode, event, true );
							}
						});
						elem._change_attached = true;
					}
				});
			},

			handle: function( event ) {
				var elem = event.target;

				// Swallow native change events from checkbox/radio, we already triggered them above
				if ( this !== elem || event.isSimulated || event.isTrigger || (elem.type !== "radio" && elem.type !== "checkbox") ) {
					return event.handleObj.handler.apply( this, arguments );
				}
			},

			teardown: function() {
				teabo.event.remove( this, "._change" );

				return rformElems.test( this.nodeName );
			}
	};
}

//Create "bubbling" focus and blur events
if ( !teabo.support.focusinBubbles ) {
	teabo.each({ focus: "focusin", blur: "focusout" }, function( orig, fix ) {

		// Attach a single capturing handler while someone wants focusin/focusout
		var attaches = 0,
		handler = function( event ) {
			teabo.event.simulate( fix, event.target, teabo.event.fix( event ), true );
		};

		teabo.event.special[ fix ] = {
				setup: function() {
					if ( attaches++ === 0 ) {
						document.addEventListener( orig, handler, true );
					}
				},
				teardown: function() {
					if ( --attaches === 0 ) {
						document.removeEventListener( orig, handler, true );
					}
				}
		};
	});
}

teabo.fn.extend({

	on: function( types, selector, data, fn, /*INTERNAL*/ one ) {
		var origFn, type;

		// Types can be a map of types/handlers
		if ( typeof types === "object" ) {
			// ( types-Object, selector, data )
			if ( typeof selector !== "string" ) { // && selector != null
				// ( types-Object, data )
				data = data || selector;
				selector = undefined;
			}
			for ( type in types ) {
				this.on( type, selector, data, types[ type ], one );
			}
			return this;
		}

		if ( data == null && fn == null ) {
			// ( types, fn )
			fn = selector;
			data = selector = undefined;
		} else if ( fn == null ) {
			if ( typeof selector === "string" ) {
				// ( types, selector, fn )
				fn = data;
				data = undefined;
			} else {
				// ( types, data, fn )
				fn = data;
				data = selector;
				selector = undefined;
			}
		}
		if ( fn === false ) {
			fn = returnFalse;
		} else if ( !fn ) {
			return this;
		}

		if ( one === 1 ) {
			origFn = fn;
			fn = function( event ) {
				// Can use an empty set, since event contains the info
				teabo().off( event );
				return origFn.apply( this, arguments );
			};
			// Use same guid so caller can remove using origFn
			fn.guid = origFn.guid || ( origFn.guid = teabo.guid++ );
		}
		return this.each( function() {
			teabo.event.add( this, types, fn, data, selector );
		});
	},
	one: function( types, selector, data, fn ) {
		return this.on( types, selector, data, fn, 1 );
	},
	off: function( types, selector, fn ) {
		if ( types && types.preventDefault && types.handleObj ) {
			// ( event )  dispatched teabo.Event
			var handleObj = types.handleObj;
			teabo( types.delegateTarget ).off(
					handleObj.namespace ? handleObj.origType + "." + handleObj.namespace : handleObj.origType,
							handleObj.selector,
							handleObj.handler
			);
			return this;
		}
		if ( typeof types === "object" ) {
			// ( types-object [, selector] )
			for ( var type in types ) {
				this.off( type, selector, types[ type ] );
			}
			return this;
		}
		if ( selector === false || typeof selector === "function" ) {
			// ( types [, fn] )
			fn = selector;
			selector = undefined;
		}
		if ( fn === false ) {
			fn = returnFalse;
		}
		return this.each(function() {
			teabo.event.remove( this, types, fn, selector );
		});
	},

	bind: function( types, data, fn ) {
		return this.on( types, null, data, fn );
	},
	unbind: function( types, fn ) {
		return this.off( types, null, fn );
	},

	live: function( types, data, fn ) {
		teabo( this.context ).on( types, this.selector, data, fn );
		return this;
	},
	die: function( types, fn ) {
		teabo( this.context ).off( types, this.selector || "**", fn );
		return this;
	},

	delegate: function( selector, types, data, fn ) {
		return this.on( types, selector, data, fn );
	},
	undelegate: function( selector, types, fn ) {
		// ( namespace ) or ( selector, types [, fn] )
		return arguments.length == 1? this.off( selector, "**" ) : this.off( types, selector, fn );
	},

	trigger: function( type, data ) {
		return this.each(function() {
			teabo.event.trigger( type, data, this );
		});
	},
	triggerHandler: function( type, data ) {
		if ( this[0] ) {
			return teabo.event.trigger( type, data, this[0], true );
		}
	},

	toggle: function( fn ) {
		// Save reference to arguments for access in closure
		var args = arguments,
		guid = fn.guid || teabo.guid++,
		i = 0,
		toggler = function( event ) {
			// Figure out which function to execute
			var lastToggle = ( teabo._data( this, "lastToggle" + fn.guid ) || 0 ) % i;
			teabo._data( this, "lastToggle" + fn.guid, lastToggle + 1 );

			// Make sure that clicks stop
			event.preventDefault();

			// and execute the function
			return args[ lastToggle ].apply( this, arguments ) || false;
		};

		// link all the functions, so any of them can unbind this click handler
		toggler.guid = guid;
		while ( i < args.length ) {
			args[ i++ ].guid = guid;
		}

		return this.click( toggler );
	},

	hover: function( fnOver, fnOut ) {
		return this.mouseenter( fnOver ).mouseleave( fnOut || fnOver );
	}
});

teabo.each( ("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave " +
"change select submit keydown keypress keyup error contextmenu").split(" "), function( i, name ) {

//	Handle event binding
	teabo.fn[ name ] = function( data, fn ) {
		if ( fn == null ) {
			fn = data;
			data = null;
		}

		return arguments.length > 0 ?
				this.on( name, null, data, fn ) :
					this.trigger( name );
	};

	if ( teabo.attrFn ) {
		teabo.attrFn[ name ] = true;
	}

	if ( rkeyEvent.test( name ) ) {
		teabo.event.fixHooks[ name ] = teabo.event.keyHooks;
	}

	if ( rmouseEvent.test( name ) ) {
		teabo.event.fixHooks[ name ] = teabo.event.mouseHooks;
	}
});


/*!
 * Sizzle CSS Selector Engine
 *  Copyright 2011, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 */
(function(){

var chunker = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g,
	expando = "sizcache" + (Math.random() + '').replace('.', ''),
	done = 0,
	toString = Object.prototype.toString,
	hasDuplicate = false,
	baseHasDuplicate = true,
	rBackslash = /\\/g,
	rReturn = /\r\n/g,
	rNonWord = /\W/;

// Here we check if the JavaScript engine is using some sort of
// optimization where it does not always call our comparision
// function. If that is the case, discard the hasDuplicate value.
//   Thus far that includes Google Chrome.
[0, 0].sort(function() {
	baseHasDuplicate = false;
	return 0;
});

var Sizzle = function( selector, context, results, seed ) {
	results = results || [];
	context = context || document;

	var origContext = context;

	if ( context.nodeType !== 1 && context.nodeType !== 9 ) {
		return [];
	}

	if ( !selector || typeof selector !== "string" ) {
		return results;
	}

	var m, set, checkSet, extra, ret, cur, pop, i,
		prune = true,
		contextXML = Sizzle.isXML( context ),
		parts = [],
		soFar = selector;

	// Reset the position of the chunker regexp (start from head)
	do {
		chunker.exec( "" );
		m = chunker.exec( soFar );

		if ( m ) {
			soFar = m[3];

			parts.push( m[1] );

			if ( m[2] ) {
				extra = m[3];
				break;
			}
		}
	} while ( m );

	if ( parts.length > 1 && origPOS.exec( selector ) ) {

		if ( parts.length === 2 && Expr.relative[ parts[0] ] ) {
			set = posProcess( parts[0] + parts[1], context, seed );

		} else {
			set = Expr.relative[ parts[0] ] ?
				[ context ] :
				Sizzle( parts.shift(), context );

			while ( parts.length ) {
				selector = parts.shift();

				if ( Expr.relative[ selector ] ) {
					selector += parts.shift();
				}

				set = posProcess( selector, set, seed );
			}
		}


	} else {
		// Take a shortcut and set the context if the root selector is an ID
		// (but not if it'll be faster if the inner selector is an ID)
		if ( !seed && parts.length > 1 && context.nodeType === 9 && !contextXML &&
				Expr.match.ID.test(parts[0]) && !Expr.match.ID.test(parts[parts.length - 1]) ) {

			ret = Sizzle.find( parts.shift(), context, contextXML );
			context = ret.expr ?
				Sizzle.filter( ret.expr, ret.set )[0] :
				ret.set[0];
		}

		if ( context ) {
			ret = seed ?
				{ expr: parts.pop(), set: makeArray(seed) } :
				Sizzle.find( parts.pop(), parts.length === 1 && (parts[0] === "~" || parts[0] === "+") && context.parentNode ? context.parentNode : context, contextXML );

			set = ret.expr ?
				Sizzle.filter( ret.expr, ret.set ) :
				ret.set;

			if ( parts.length > 0 ) {
				checkSet = makeArray( set );

			} else {
				prune = false;
			}

			while ( parts.length ) {
				cur = parts.pop();
				pop = cur;

				if ( !Expr.relative[ cur ] ) {
					cur = "";
				} else {
					pop = parts.pop();
				}

				if ( pop == null ) {
					pop = context;
				}

				Expr.relative[ cur ]( checkSet, pop, contextXML );
			}

		} else {
			checkSet = parts = [];
		}
	}

	if ( !checkSet ) {
		checkSet = set;
	}

	if ( !checkSet ) {
		Sizzle.error( cur || selector );
	}

	if ( toString.call(checkSet) === "[object Array]" ) {
		if ( !prune ) {
			results.push.apply( results, checkSet );

		} else if ( context && context.nodeType === 1 ) {
			for ( i = 0; checkSet[i] != null; i++ ) {
				if ( checkSet[i] && (checkSet[i] === true || checkSet[i].nodeType === 1 && Sizzle.contains(context, checkSet[i])) ) {
					results.push( set[i] );
				}
			}

		} else {
			for ( i = 0; checkSet[i] != null; i++ ) {
				if ( checkSet[i] && checkSet[i].nodeType === 1 ) {
					results.push( set[i] );
				}
			}
		}

	} else {
		makeArray( checkSet, results );
	}

	if ( extra ) {
		Sizzle( extra, origContext, results, seed );
		Sizzle.uniqueSort( results );
	}

	return results;
};

Sizzle.uniqueSort = function( results ) {
	if ( sortOrder ) {
		hasDuplicate = baseHasDuplicate;
		results.sort( sortOrder );

		if ( hasDuplicate ) {
			for ( var i = 1; i < results.length; i++ ) {
				if ( results[i] === results[ i - 1 ] ) {
					results.splice( i--, 1 );
				}
			}
		}
	}

	return results;
};

Sizzle.matches = function( expr, set ) {
	return Sizzle( expr, null, null, set );
};

Sizzle.matchesSelector = function( node, expr ) {
	return Sizzle( expr, null, null, [node] ).length > 0;
};

Sizzle.find = function( expr, context, isXML ) {
	var set, i, len, match, type, left;

	if ( !expr ) {
		return [];
	}

	for ( i = 0, len = Expr.order.length; i < len; i++ ) {
		type = Expr.order[i];

		if ( (match = Expr.leftMatch[ type ].exec( expr )) ) {
			left = match[1];
			match.splice( 1, 1 );

			if ( left.substr( left.length - 1 ) !== "\\" ) {
				match[1] = (match[1] || "").replace( rBackslash, "" );
				set = Expr.find[ type ]( match, context, isXML );

				if ( set != null ) {
					expr = expr.replace( Expr.match[ type ], "" );
					break;
				}
			}
		}
	}

	if ( !set ) {
		set = typeof context.getElementsByTagName !== "undefined" ?
			context.getElementsByTagName( "*" ) :
			[];
	}

	return { set: set, expr: expr };
};

Sizzle.filter = function( expr, set, inplace, not ) {
	var match, anyFound,
		type, found, item, filter, left,
		i, pass,
		old = expr,
		result = [],
		curLoop = set,
		isXMLFilter = set && set[0] && Sizzle.isXML( set[0] );

	while ( expr && set.length ) {
		for ( type in Expr.filter ) {
			if ( (match = Expr.leftMatch[ type ].exec( expr )) != null && match[2] ) {
				filter = Expr.filter[ type ];
				left = match[1];

				anyFound = false;

				match.splice(1,1);

				if ( left.substr( left.length - 1 ) === "\\" ) {
					continue;
				}

				if ( curLoop === result ) {
					result = [];
				}

				if ( Expr.preFilter[ type ] ) {
					match = Expr.preFilter[ type ]( match, curLoop, inplace, result, not, isXMLFilter );

					if ( !match ) {
						anyFound = found = true;

					} else if ( match === true ) {
						continue;
					}
				}

				if ( match ) {
					for ( i = 0; (item = curLoop[i]) != null; i++ ) {
						if ( item ) {
							found = filter( item, match, i, curLoop );
							pass = not ^ found;

							if ( inplace && found != null ) {
								if ( pass ) {
									anyFound = true;

								} else {
									curLoop[i] = false;
								}

							} else if ( pass ) {
								result.push( item );
								anyFound = true;
							}
						}
					}
				}

				if ( found !== undefined ) {
					if ( !inplace ) {
						curLoop = result;
					}

					expr = expr.replace( Expr.match[ type ], "" );

					if ( !anyFound ) {
						return [];
					}

					break;
				}
			}
		}

		// Improper expression
		if ( expr === old ) {
			if ( anyFound == null ) {
				Sizzle.error( expr );

			} else {
				break;
			}
		}

		old = expr;
	}

	return curLoop;
};

Sizzle.error = function( msg ) {
	throw new Error( "Syntax error, unrecognized expression: " + msg );
};

/**
 * Utility function for retreiving the text value of an array of DOM nodes
 * @param {Array|Element} elem
 */
var getText = Sizzle.getText = function( elem ) {
    var i, node,
		nodeType = elem.nodeType,
		ret = "";

	if ( nodeType ) {
		if ( nodeType === 1 || nodeType === 9 || nodeType === 11 ) {
			// Use textContent || innerText for elements
			if ( typeof elem.textContent === 'string' ) {
				return elem.textContent;
			} else if ( typeof elem.innerText === 'string' ) {
				// Replace IE's carriage returns
				return elem.innerText.replace( rReturn, '' );
			} else {
				// Traverse it's children
				for ( elem = elem.firstChild; elem; elem = elem.nextSibling) {
					ret += getText( elem );
				}
			}
		} else if ( nodeType === 3 || nodeType === 4 ) {
			return elem.nodeValue;
		}
	} else {

		// If no nodeType, this is expected to be an array
		for ( i = 0; (node = elem[i]); i++ ) {
			// Do not traverse comment nodes
			if ( node.nodeType !== 8 ) {
				ret += getText( node );
			}
		}
	}
	return ret;
};

var Expr = Sizzle.selectors = {
	order: [ "ID", "NAME", "TAG" ],

	match: {
		ID: /#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
		CLASS: /\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
		NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/,
		ATTR: /\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(?:(['"])(.*?)\3|(#?(?:[\w\u00c0-\uFFFF\-]|\\.)*)|)|)\s*\]/,
		TAG: /^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/,
		CHILD: /:(only|nth|last|first)-child(?:\(\s*(even|odd|(?:[+\-]?\d+|(?:[+\-]?\d*)?n\s*(?:[+\-]\s*\d+)?))\s*\))?/,
		POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/,
		PSEUDO: /:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/
	},

	leftMatch: {},

	attrMap: {
		"class": "className",
		"for": "htmlFor"
	},

	attrHandle: {
		href: function( elem ) {
			return elem.getAttribute( "href" );
		},
		type: function( elem ) {
			return elem.getAttribute( "type" );
		}
	},

	relative: {
		"+": function(checkSet, part){
			var isPartStr = typeof part === "string",
				isTag = isPartStr && !rNonWord.test( part ),
				isPartStrNotTag = isPartStr && !isTag;

			if ( isTag ) {
				part = part.toLowerCase();
			}

			for ( var i = 0, l = checkSet.length, elem; i < l; i++ ) {
				if ( (elem = checkSet[i]) ) {
					while ( (elem = elem.previousSibling) && elem.nodeType !== 1 ) {}

					checkSet[i] = isPartStrNotTag || elem && elem.nodeName.toLowerCase() === part ?
						elem || false :
						elem === part;
				}
			}

			if ( isPartStrNotTag ) {
				Sizzle.filter( part, checkSet, true );
			}
		},

		">": function( checkSet, part ) {
			var elem,
				isPartStr = typeof part === "string",
				i = 0,
				l = checkSet.length;

			if ( isPartStr && !rNonWord.test( part ) ) {
				part = part.toLowerCase();

				for ( ; i < l; i++ ) {
					elem = checkSet[i];

					if ( elem ) {
						var parent = elem.parentNode;
						checkSet[i] = parent.nodeName.toLowerCase() === part ? parent : false;
					}
				}

			} else {
				for ( ; i < l; i++ ) {
					elem = checkSet[i];

					if ( elem ) {
						checkSet[i] = isPartStr ?
							elem.parentNode :
							elem.parentNode === part;
					}
				}

				if ( isPartStr ) {
					Sizzle.filter( part, checkSet, true );
				}
			}
		},

		"": function(checkSet, part, isXML){
			var nodeCheck,
				doneName = done++,
				checkFn = dirCheck;

			if ( typeof part === "string" && !rNonWord.test( part ) ) {
				part = part.toLowerCase();
				nodeCheck = part;
				checkFn = dirNodeCheck;
			}

			checkFn( "parentNode", part, doneName, checkSet, nodeCheck, isXML );
		},

		"~": function( checkSet, part, isXML ) {
			var nodeCheck,
				doneName = done++,
				checkFn = dirCheck;

			if ( typeof part === "string" && !rNonWord.test( part ) ) {
				part = part.toLowerCase();
				nodeCheck = part;
				checkFn = dirNodeCheck;
			}

			checkFn( "previousSibling", part, doneName, checkSet, nodeCheck, isXML );
		}
	},

	find: {
		ID: function( match, context, isXML ) {
			if ( typeof context.getElementById !== "undefined" && !isXML ) {
				var m = context.getElementById(match[1]);
				// Check parentNode to catch when Blackberry 4.6 returns
				// nodes that are no longer in the document #6963
				return m && m.parentNode ? [m] : [];
			}
		},

		NAME: function( match, context ) {
			if ( typeof context.getElementsByName !== "undefined" ) {
				var ret = [],
					results = context.getElementsByName( match[1] );

				for ( var i = 0, l = results.length; i < l; i++ ) {
					if ( results[i].getAttribute("name") === match[1] ) {
						ret.push( results[i] );
					}
				}

				return ret.length === 0 ? null : ret;
			}
		},

		TAG: function( match, context ) {
			if ( typeof context.getElementsByTagName !== "undefined" ) {
				return context.getElementsByTagName( match[1] );
			}
		}
	},
	preFilter: {
		CLASS: function( match, curLoop, inplace, result, not, isXML ) {
			match = " " + match[1].replace( rBackslash, "" ) + " ";

			if ( isXML ) {
				return match;
			}

			for ( var i = 0, elem; (elem = curLoop[i]) != null; i++ ) {
				if ( elem ) {
					if ( not ^ (elem.className && (" " + elem.className + " ").replace(/[\t\n\r]/g, " ").indexOf(match) >= 0) ) {
						if ( !inplace ) {
							result.push( elem );
						}

					} else if ( inplace ) {
						curLoop[i] = false;
					}
				}
			}

			return false;
		},

		ID: function( match ) {
			return match[1].replace( rBackslash, "" );
		},

		TAG: function( match, curLoop ) {
			return match[1].replace( rBackslash, "" ).toLowerCase();
		},

		CHILD: function( match ) {
			if ( match[1] === "nth" ) {
				if ( !match[2] ) {
					Sizzle.error( match[0] );
				}

				match[2] = match[2].replace(/^\+|\s*/g, '');

				// parse equations like 'even', 'odd', '5', '2n', '3n+2', '4n-1', '-n+6'
				var test = /(-?)(\d*)(?:n([+\-]?\d*))?/.exec(
					match[2] === "even" && "2n" || match[2] === "odd" && "2n+1" ||
					!/\D/.test( match[2] ) && "0n+" + match[2] || match[2]);

				// calculate the numbers (first)n+(last) including if they are negative
				match[2] = (test[1] + (test[2] || 1)) - 0;
				match[3] = test[3] - 0;
			}
			else if ( match[2] ) {
				Sizzle.error( match[0] );
			}

			// TODO: Move to normal caching system
			match[0] = done++;

			return match;
		},

		ATTR: function( match, curLoop, inplace, result, not, isXML ) {
			var name = match[1] = match[1].replace( rBackslash, "" );

			if ( !isXML && Expr.attrMap[name] ) {
				match[1] = Expr.attrMap[name];
			}

			// Handle if an un-quoted value was used
			match[4] = ( match[4] || match[5] || "" ).replace( rBackslash, "" );

			if ( match[2] === "~=" ) {
				match[4] = " " + match[4] + " ";
			}

			return match;
		},

		PSEUDO: function( match, curLoop, inplace, result, not ) {
			if ( match[1] === "not" ) {
				// If we're dealing with a complex expression, or a simple one
				if ( ( chunker.exec(match[3]) || "" ).length > 1 || /^\w/.test(match[3]) ) {
					match[3] = Sizzle(match[3], null, null, curLoop);

				} else {
					var ret = Sizzle.filter(match[3], curLoop, inplace, true ^ not);

					if ( !inplace ) {
						result.push.apply( result, ret );
					}

					return false;
				}

			} else if ( Expr.match.POS.test( match[0] ) || Expr.match.CHILD.test( match[0] ) ) {
				return true;
			}

			return match;
		},

		POS: function( match ) {
			match.unshift( true );

			return match;
		}
	},

	filters: {
		enabled: function( elem ) {
			return elem.disabled === false && elem.type !== "hidden";
		},

		disabled: function( elem ) {
			return elem.disabled === true;
		},

		checked: function( elem ) {
			return elem.checked === true;
		},

		selected: function( elem ) {
			// Accessing this property makes selected-by-default
			// options in Safari work properly
			if ( elem.parentNode ) {
				elem.parentNode.selectedIndex;
			}

			return elem.selected === true;
		},

		parent: function( elem ) {
			return !!elem.firstChild;
		},

		empty: function( elem ) {
			return !elem.firstChild;
		},

		has: function( elem, i, match ) {
			return !!Sizzle( match[3], elem ).length;
		},

		header: function( elem ) {
			return (/h\d/i).test( elem.nodeName );
		},

		text: function( elem ) {
			var attr = elem.getAttribute( "type" ), type = elem.type;
			// IE6 and 7 will map elem.type to 'text' for new HTML5 types (search, etc)
			// use getAttribute instead to test this case
			return elem.nodeName.toLowerCase() === "input" && "text" === type && ( attr === type || attr === null );
		},

		radio: function( elem ) {
			return elem.nodeName.toLowerCase() === "input" && "radio" === elem.type;
		},

		checkbox: function( elem ) {
			return elem.nodeName.toLowerCase() === "input" && "checkbox" === elem.type;
		},

		file: function( elem ) {
			return elem.nodeName.toLowerCase() === "input" && "file" === elem.type;
		},

		password: function( elem ) {
			return elem.nodeName.toLowerCase() === "input" && "password" === elem.type;
		},

		submit: function( elem ) {
			var name = elem.nodeName.toLowerCase();
			return (name === "input" || name === "button") && "submit" === elem.type;
		},

		image: function( elem ) {
			return elem.nodeName.toLowerCase() === "input" && "image" === elem.type;
		},

		reset: function( elem ) {
			var name = elem.nodeName.toLowerCase();
			return (name === "input" || name === "button") && "reset" === elem.type;
		},

		button: function( elem ) {
			var name = elem.nodeName.toLowerCase();
			return name === "input" && "button" === elem.type || name === "button";
		},

		input: function( elem ) {
			return (/input|select|textarea|button/i).test( elem.nodeName );
		},

		focus: function( elem ) {
			return elem === elem.ownerDocument.activeElement;
		}
	},
	setFilters: {
		first: function( elem, i ) {
			return i === 0;
		},

		last: function( elem, i, match, array ) {
			return i === array.length - 1;
		},

		even: function( elem, i ) {
			return i % 2 === 0;
		},

		odd: function( elem, i ) {
			return i % 2 === 1;
		},

		lt: function( elem, i, match ) {
			return i < match[3] - 0;
		},

		gt: function( elem, i, match ) {
			return i > match[3] - 0;
		},

		nth: function( elem, i, match ) {
			return match[3] - 0 === i;
		},

		eq: function( elem, i, match ) {
			return match[3] - 0 === i;
		}
	},
	filter: {
		PSEUDO: function( elem, match, i, array ) {
			var name = match[1],
				filter = Expr.filters[ name ];

			if ( filter ) {
				return filter( elem, i, match, array );

			} else if ( name === "contains" ) {
				return (elem.textContent || elem.innerText || getText([ elem ]) || "").indexOf(match[3]) >= 0;

			} else if ( name === "not" ) {
				var not = match[3];

				for ( var j = 0, l = not.length; j < l; j++ ) {
					if ( not[j] === elem ) {
						return false;
					}
				}

				return true;

			} else {
				Sizzle.error( name );
			}
		},

		CHILD: function( elem, match ) {
			var first, last,
				doneName, parent, cache,
				count, diff,
				type = match[1],
				node = elem;

			switch ( type ) {
				case "only":
				case "first":
					while ( (node = node.previousSibling) ) {
						if ( node.nodeType === 1 ) {
							return false;
						}
					}

					if ( type === "first" ) {
						return true;
					}

					node = elem;

					/* falls through */
				case "last":
					while ( (node = node.nextSibling) ) {
						if ( node.nodeType === 1 ) {
							return false;
						}
					}

					return true;

				case "nth":
					first = match[2];
					last = match[3];

					if ( first === 1 && last === 0 ) {
						return true;
					}

					doneName = match[0];
					parent = elem.parentNode;

					if ( parent && (parent[ expando ] !== doneName || !elem.nodeIndex) ) {
						count = 0;

						for ( node = parent.firstChild; node; node = node.nextSibling ) {
							if ( node.nodeType === 1 ) {
								node.nodeIndex = ++count;
							}
						}

						parent[ expando ] = doneName;
					}

					diff = elem.nodeIndex - last;

					if ( first === 0 ) {
						return diff === 0;

					} else {
						return ( diff % first === 0 && diff / first >= 0 );
					}
			}
		},

		ID: function( elem, match ) {
			return elem.nodeType === 1 && elem.getAttribute("id") === match;
		},

		TAG: function( elem, match ) {
			return (match === "*" && elem.nodeType === 1) || !!elem.nodeName && elem.nodeName.toLowerCase() === match;
		},

		CLASS: function( elem, match ) {
			return (" " + (elem.className || elem.getAttribute("class")) + " ")
				.indexOf( match ) > -1;
		},

		ATTR: function( elem, match ) {
			var name = match[1],
				result = Sizzle.attr ?
					Sizzle.attr( elem, name ) :
					Expr.attrHandle[ name ] ?
					Expr.attrHandle[ name ]( elem ) :
					elem[ name ] != null ?
						elem[ name ] :
						elem.getAttribute( name ),
				value = result + "",
				type = match[2],
				check = match[4];

			return result == null ?
				type === "!=" :
				!type && Sizzle.attr ?
				result != null :
				type === "=" ?
				value === check :
				type === "*=" ?
				value.indexOf(check) >= 0 :
				type === "~=" ?
				(" " + value + " ").indexOf(check) >= 0 :
				!check ?
				value && result !== false :
				type === "!=" ?
				value !== check :
				type === "^=" ?
				value.indexOf(check) === 0 :
				type === "$=" ?
				value.substr(value.length - check.length) === check :
				type === "|=" ?
				value === check || value.substr(0, check.length + 1) === check + "-" :
				false;
		},

		POS: function( elem, match, i, array ) {
			var name = match[2],
				filter = Expr.setFilters[ name ];

			if ( filter ) {
				return filter( elem, i, match, array );
			}
		}
	}
};

var origPOS = Expr.match.POS,
	fescape = function(all, num){
		return "\\" + (num - 0 + 1);
	};

for ( var type in Expr.match ) {
	Expr.match[ type ] = new RegExp( Expr.match[ type ].source + (/(?![^\[]*\])(?![^\(]*\))/.source) );
	Expr.leftMatch[ type ] = new RegExp( /(^(?:.|\r|\n)*?)/.source + Expr.match[ type ].source.replace(/\\(\d+)/g, fescape) );
}
// Expose origPOS
// "global" as in regardless of relation to brackets/parens
Expr.match.globalPOS = origPOS;

var makeArray = function( array, results ) {
	array = Array.prototype.slice.call( array, 0 );

	if ( results ) {
		results.push.apply( results, array );
		return results;
	}

	return array;
};

// Perform a simple check to determine if the browser is capable of
// converting a NodeList to an array using builtin methods.
// Also verifies that the returned array holds DOM nodes
// (which is not the case in the Blackberry browser)
try {
	Array.prototype.slice.call( document.documentElement.childNodes, 0 )[0].nodeType;

// Provide a fallback method if it does not work
} catch( e ) {
	makeArray = function( array, results ) {
		var i = 0,
			ret = results || [];

		if ( toString.call(array) === "[object Array]" ) {
			Array.prototype.push.apply( ret, array );

		} else {
			if ( typeof array.length === "number" ) {
				for ( var l = array.length; i < l; i++ ) {
					ret.push( array[i] );
				}

			} else {
				for ( ; array[i]; i++ ) {
					ret.push( array[i] );
				}
			}
		}

		return ret;
	};
}

var sortOrder, siblingCheck;

if ( document.documentElement.compareDocumentPosition ) {
	sortOrder = function( a, b ) {
		if ( a === b ) {
			hasDuplicate = true;
			return 0;
		}

		if ( !a.compareDocumentPosition || !b.compareDocumentPosition ) {
			return a.compareDocumentPosition ? -1 : 1;
		}

		return a.compareDocumentPosition(b) & 4 ? -1 : 1;
	};

} else {
	sortOrder = function( a, b ) {
		// The nodes are identical, we can exit early
		if ( a === b ) {
			hasDuplicate = true;
			return 0;

		// Fallback to using sourceIndex (in IE) if it's available on both nodes
		} else if ( a.sourceIndex && b.sourceIndex ) {
			return a.sourceIndex - b.sourceIndex;
		}

		var al, bl,
			ap = [],
			bp = [],
			aup = a.parentNode,
			bup = b.parentNode,
			cur = aup;

		// If the nodes are siblings (or identical) we can do a quick check
		if ( aup === bup ) {
			return siblingCheck( a, b );

		// If no parents were found then the nodes are disconnected
		} else if ( !aup ) {
			return -1;

		} else if ( !bup ) {
			return 1;
		}

		// Otherwise they're somewhere else in the tree so we need
		// to build up a full list of the parentNodes for comparison
		while ( cur ) {
			ap.unshift( cur );
			cur = cur.parentNode;
		}

		cur = bup;

		while ( cur ) {
			bp.unshift( cur );
			cur = cur.parentNode;
		}

		al = ap.length;
		bl = bp.length;

		// Start walking down the tree looking for a discrepancy
		for ( var i = 0; i < al && i < bl; i++ ) {
			if ( ap[i] !== bp[i] ) {
				return siblingCheck( ap[i], bp[i] );
			}
		}

		// We ended someplace up the tree so do a sibling check
		return i === al ?
			siblingCheck( a, bp[i], -1 ) :
			siblingCheck( ap[i], b, 1 );
	};

	siblingCheck = function( a, b, ret ) {
		if ( a === b ) {
			return ret;
		}

		var cur = a.nextSibling;

		while ( cur ) {
			if ( cur === b ) {
				return -1;
			}

			cur = cur.nextSibling;
		}

		return 1;
	};
}

// Check to see if the browser returns elements by name when
// querying by getElementById (and provide a workaround)
(function(){
	// We're going to inject a fake input element with a specified name
	var form = document.createElement("div"),
		id = "script" + (new Date()).getTime(),
		root = document.documentElement;

	form.innerHTML = "<a name='" + id + "'/>";

	// Inject it into the root element, check its status, and remove it quickly
	root.insertBefore( form, root.firstChild );

	// The workaround has to do additional checks after a getElementById
	// Which slows things down for other browsers (hence the branching)
	if ( document.getElementById( id ) ) {
		Expr.find.ID = function( match, context, isXML ) {
			if ( typeof context.getElementById !== "undefined" && !isXML ) {
				var m = context.getElementById(match[1]);

				return m ?
					m.id === match[1] || typeof m.getAttributeNode !== "undefined" && m.getAttributeNode("id").nodeValue === match[1] ?
						[m] :
						undefined :
					[];
			}
		};

		Expr.filter.ID = function( elem, match ) {
			var node = typeof elem.getAttributeNode !== "undefined" && elem.getAttributeNode("id");

			return elem.nodeType === 1 && node && node.nodeValue === match;
		};
	}

	root.removeChild( form );

	// release memory in IE
	root = form = null;
})();

(function(){
	// Check to see if the browser returns only elements
	// when doing getElementsByTagName("*")

	// Create a fake element
	var div = document.createElement("div");
	div.appendChild( document.createComment("") );

	// Make sure no comments are found
	if ( div.getElementsByTagName("*").length > 0 ) {
		Expr.find.TAG = function( match, context ) {
			var results = context.getElementsByTagName( match[1] );

			// Filter out possible comments
			if ( match[1] === "*" ) {
				var tmp = [];

				for ( var i = 0; results[i]; i++ ) {
					if ( results[i].nodeType === 1 ) {
						tmp.push( results[i] );
					}
				}

				results = tmp;
			}

			return results;
		};
	}

	// Check to see if an attribute returns normalized href attributes
	div.innerHTML = "<a href='#'></a>";

	if ( div.firstChild && typeof div.firstChild.getAttribute !== "undefined" &&
			div.firstChild.getAttribute("href") !== "#" ) {

		Expr.attrHandle.href = function( elem ) {
			return elem.getAttribute( "href", 2 );
		};
	}

	// release memory in IE
	div = null;
})();

if ( document.querySelectorAll ) {
	(function(){
		var oldSizzle = Sizzle,
			div = document.createElement("div"),
			id = "__sizzle__";

		div.innerHTML = "<p class='TEST'></p>";

		// Safari can't handle uppercase or unicode characters when
		// in quirks mode.
		if ( div.querySelectorAll && div.querySelectorAll(".TEST").length === 0 ) {
			return;
		}

		Sizzle = function( query, context, extra, seed ) {
			context = context || document;

			// Only use querySelectorAll on non-XML documents
			// (ID selectors don't work in non-HTML documents)
			if ( !seed && !Sizzle.isXML(context) ) {
				// See if we find a selector to speed up
				var match = /^(\w+$)|^\.([\w\-]+$)|^#([\w\-]+$)/.exec( query );

				if ( match && (context.nodeType === 1 || context.nodeType === 9) ) {
					// Speed-up: Sizzle("TAG")
					if ( match[1] ) {
						return makeArray( context.getElementsByTagName( query ), extra );

					// Speed-up: Sizzle(".CLASS")
					} else if ( match[2] && Expr.find.CLASS && context.getElementsByClassName ) {
						return makeArray( context.getElementsByClassName( match[2] ), extra );
					}
				}

				if ( context.nodeType === 9 ) {
					// Speed-up: Sizzle("body")
					// The body element only exists once, optimize finding it
					if ( query === "body" && context.body ) {
						return makeArray( [ context.body ], extra );

					// Speed-up: Sizzle("#ID")
					} else if ( match && match[3] ) {
						var elem = context.getElementById( match[3] );

						// Check parentNode to catch when Blackberry 4.6 returns
						// nodes that are no longer in the document #6963
						if ( elem && elem.parentNode ) {
							// Handle the case where IE and Opera return items
							// by name instead of ID
							if ( elem.id === match[3] ) {
								return makeArray( [ elem ], extra );
							}

						} else {
							return makeArray( [], extra );
						}
					}

					try {
						return makeArray( context.querySelectorAll(query), extra );
					} catch(qsaError) {}

				// qSA works strangely on Element-rooted queries
				// We can work around this by specifying an extra ID on the root
				// and working up from there (Thanks to Andrew Dupont for the technique)
				// IE 8 doesn't work on object elements
				} else if ( context.nodeType === 1 && context.nodeName.toLowerCase() !== "object" ) {
					var oldContext = context,
						old = context.getAttribute( "id" ),
						nid = old || id,
						hasParent = context.parentNode,
						relativeHierarchySelector = /^\s*[+~]/.test( query );

					if ( !old ) {
						context.setAttribute( "id", nid );
					} else {
						nid = nid.replace( /'/g, "\\$&" );
					}
					if ( relativeHierarchySelector && hasParent ) {
						context = context.parentNode;
					}

					try {
						if ( !relativeHierarchySelector || hasParent ) {
							return makeArray( context.querySelectorAll( "[id='" + nid + "'] " + query ), extra );
						}

					} catch(pseudoError) {
					} finally {
						if ( !old ) {
							oldContext.removeAttribute( "id" );
						}
					}
				}
			}

			return oldSizzle(query, context, extra, seed);
		};

		for ( var prop in oldSizzle ) {
			Sizzle[ prop ] = oldSizzle[ prop ];
		}

		// release memory in IE
		div = null;
	})();
}

(function(){
	var html = document.documentElement,
		matches = html.matchesSelector || html.mozMatchesSelector || html.webkitMatchesSelector || html.msMatchesSelector;

	if ( matches ) {
		// Check to see if it's possible to do matchesSelector
		// on a disconnected node (IE 9 fails this)
		var disconnectedMatch = !matches.call( document.createElement( "div" ), "div" ),
			pseudoWorks = false;

		try {
			// This should fail with an exception
			// Gecko does not error, returns false instead
			matches.call( document.documentElement, "[test!='']:sizzle" );

		} catch( pseudoError ) {
			pseudoWorks = true;
		}

		Sizzle.matchesSelector = function( node, expr ) {
			// Make sure that attribute selectors are quoted
			expr = expr.replace(/\=\s*([^'"\]]*)\s*\]/g, "='$1']");

			if ( !Sizzle.isXML( node ) ) {
				try {
					if ( pseudoWorks || !Expr.match.PSEUDO.test( expr ) && !/!=/.test( expr ) ) {
						var ret = matches.call( node, expr );

						// IE 9's matchesSelector returns false on disconnected nodes
						if ( ret || !disconnectedMatch ||
								// As well, disconnected nodes are said to be in a document
								// fragment in IE 9, so check for that
								node.document && node.document.nodeType !== 11 ) {
							return ret;
						}
					}
				} catch(e) {}
			}

			return Sizzle(expr, null, null, [node]).length > 0;
		};
	}
})();

(function(){
	var div = document.createElement("div");

	div.innerHTML = "<div class='test e'></div><div class='test'></div>";

	// Opera can't find a second classname (in 9.6)
	// Also, make sure that getElementsByClassName actually exists
	if ( !div.getElementsByClassName || div.getElementsByClassName("e").length === 0 ) {
		return;
	}

	// Safari caches class attributes, doesn't catch changes (in 3.2)
	div.lastChild.className = "e";

	if ( div.getElementsByClassName("e").length === 1 ) {
		return;
	}

	Expr.order.splice(1, 0, "CLASS");
	Expr.find.CLASS = function( match, context, isXML ) {
		if ( typeof context.getElementsByClassName !== "undefined" && !isXML ) {
			return context.getElementsByClassName(match[1]);
		}
	};

	// release memory in IE
	div = null;
})();

function dirNodeCheck( dir, cur, doneName, checkSet, nodeCheck, isXML ) {
	for ( var i = 0, l = checkSet.length; i < l; i++ ) {
		var elem = checkSet[i];

		if ( elem ) {
			var match = false;

			elem = elem[dir];

			while ( elem ) {
				if ( elem[ expando ] === doneName ) {
					match = checkSet[elem.sizset];
					break;
				}

				if ( elem.nodeType === 1 && !isXML ){
					elem[ expando ] = doneName;
					elem.sizset = i;
				}

				if ( elem.nodeName.toLowerCase() === cur ) {
					match = elem;
					break;
				}

				elem = elem[dir];
			}

			checkSet[i] = match;
		}
	}
}

function dirCheck( dir, cur, doneName, checkSet, nodeCheck, isXML ) {
	for ( var i = 0, l = checkSet.length; i < l; i++ ) {
		var elem = checkSet[i];

		if ( elem ) {
			var match = false;

			elem = elem[dir];

			while ( elem ) {
				if ( elem[ expando ] === doneName ) {
					match = checkSet[elem.sizset];
					break;
				}

				if ( elem.nodeType === 1 ) {
					if ( !isXML ) {
						elem[ expando ] = doneName;
						elem.sizset = i;
					}

					if ( typeof cur !== "string" ) {
						if ( elem === cur ) {
							match = true;
							break;
						}

					} else if ( Sizzle.filter( cur, [elem] ).length > 0 ) {
						match = elem;
						break;
					}
				}

				elem = elem[dir];
			}

			checkSet[i] = match;
		}
	}
}

if ( document.documentElement.contains ) {
	Sizzle.contains = function( a, b ) {
		return a !== b && (a.contains ? a.contains(b) : true);
	};

} else if ( document.documentElement.compareDocumentPosition ) {
	Sizzle.contains = function( a, b ) {
		return !!(a.compareDocumentPosition(b) & 16);
	};

} else {
	Sizzle.contains = function() {
		return false;
	};
}

Sizzle.isXML = function( elem ) {
	// documentElement is verified for cases where it doesn't yet exist
	// (such as loading iframes in IE - #4833)
	var documentElement = (elem ? elem.ownerDocument || elem : 0).documentElement;

	return documentElement ? documentElement.nodeName !== "HTML" : false;
};

var posProcess = function( selector, context, seed ) {
	var match,
		tmpSet = [],
		later = "",
		root = context.nodeType ? [context] : context;

	// Position selectors must be done after the filter
	// And so must :not(positional) so we move all PSEUDOs to the end
	while ( (match = Expr.match.PSEUDO.exec( selector )) ) {
		later += match[0];
		selector = selector.replace( Expr.match.PSEUDO, "" );
	}

	selector = Expr.relative[selector] ? selector + "*" : selector;

	for ( var i = 0, l = root.length; i < l; i++ ) {
		Sizzle( selector, root[i], tmpSet, seed );
	}

	return Sizzle.filter( later, tmpSet );
};

// EXPOSE
// Override sizzle attribute retrieval
Sizzle.attr = teabo.attr;
Sizzle.selectors.attrMap = {};
teabo.find = Sizzle;
teabo.expr = Sizzle.selectors;
teabo.expr[":"] = teabo.expr.filters;
teabo.unique = Sizzle.uniqueSort;
teabo.text = Sizzle.getText;
teabo.isXMLDoc = Sizzle.isXML;
teabo.contains = Sizzle.contains;


})();

var runtil = /Until$/,
rparentsprev = /^(?:parents|prevUntil|prevAll)/,
// Note: This RegExp should be improved, or likely pulled from Sizzle
rmultiselector = /,/,
isSimple = /^.[^:#\[\.,]*$/,
slice = Array.prototype.slice,
POS = teabo.expr.match.globalPOS,
// methods guaranteed to produce a unique set when starting from a unique set
guaranteedUnique = {
	children: true,
	contents: true,
	next: true,
	prev: true
};

teabo.fn.extend({
	find: function( selector ) {
		var self = this,
		i, l;

		if ( typeof selector !== "string" ) {
			return teabo( selector ).filter(function() {
				for ( i = 0, l = self.length; i < l; i++ ) {
					if ( teabo.contains( self[ i ], this ) ) {
						return true;
					}
				}
			});
		}
		var ret = this.pushStack( "", "find", selector ),
		length, n, r;

		for ( i = 0, l = this.length; i < l; i++ ) {
			length = ret.length;
			teabo.find( selector, this[i], ret );

			if ( i > 0 ) {
				// Make sure that the results are unique
				for ( n = length; n < ret.length; n++ ) {
					for ( r = 0; r < length; r++ ) {
						if ( ret[r] === ret[n] ) {
							ret.splice(n--, 1);
							break;
						}
					}
				}
			}
		}

		return ret;
	},

	has: function( target ) {
		var targets = teabo( target );
		return this.filter(function() {
			for ( var i = 0, l = targets.length; i < l; i++ ) {
				if ( teabo.contains( this, targets[i] ) ) {
					return true;
				}
			}
		});
	},

	not: function( selector ) {
		return this.pushStack( winnow(this, selector, false), "not", selector);
	},

	filter: function( selector ) {
		return this.pushStack( winnow(this, selector, true), "filter", selector );
	},

	is: function( selector ) {
		return !!selector && (
				typeof selector === "string" ?
						// If this is a positional selector, check membership in the returned set
						// so $("p:first").is("p:last") won't return true for a doc with two "p".
						POS.test( selector ) ?
								teabo( selector, this.context ).index( this[0] ) >= 0 :
									teabo.filter( selector, this ).length > 0 :
										this.filter( selector ).length > 0 );
	},

	closest: function( selectors, context ) {
		var ret = [], i, l, cur = this[0];

		// Array (deprecated as of teabo 1.7)
		if ( teabo.isArray( selectors ) ) {
			var level = 1;

			while ( cur && cur.ownerDocument && cur !== context ) {
				for ( i = 0; i < selectors.length; i++ ) {

					if ( teabo( cur ).is( selectors[ i ] ) ) {
						ret.push({ selector: selectors[ i ], elem: cur, level: level });
					}
				}

				cur = cur.parentNode;
				level++;
			}

			return ret;
		}

		// String
		var pos = POS.test( selectors ) || typeof selectors !== "string" ?
				teabo( selectors, context || this.context ) :
					0;

				for ( i = 0, l = this.length; i < l; i++ ) {
					cur = this[i];

					while ( cur ) {
						if ( pos ? pos.index(cur) > -1 : teabo.find.matchesSelector(cur, selectors) ) {
							ret.push( cur );
							break;

						} else {
							cur = cur.parentNode;
							if ( !cur || !cur.ownerDocument || cur === context || cur.nodeType === 11 ) {
								break;
							}
						}
					}
				}

				ret = ret.length > 1 ? teabo.unique( ret ) : ret;

				return this.pushStack( ret, "closest", selectors );
	},

//	Determine the position of an element within
//	the matched set of elements
	index: function( elem ) {

		// No argument, return index in parent
		if ( !elem ) {
			return ( this[0] && this[0].parentNode ) ? this.prevAll().length : -1;
		}

		// index in selector
		if ( typeof elem === "string" ) {
			return teabo.inArray( this[0], teabo( elem ) );
		}

		// Locate the position of the desired element
		return teabo.inArray(
				// If it receives a teabo object, the first element is used
				elem.teabo ? elem[0] : elem, this );
	},

	add: function( selector, context ) {
		var set = typeof selector === "string" ?
				teabo( selector, context ) :
					teabo.makeArray( selector && selector.nodeType ? [ selector ] : selector ),
					all = teabo.merge( this.get(), set );

				return this.pushStack( isDisconnected( set[0] ) || isDisconnected( all[0] ) ?
						all :
							teabo.unique( all ) );
	},

	andSelf: function() {
		return this.add( this.prevObject );
	}
});

//A painfully simple check to see if an element is disconnected
//from a document (should be improved, where feasible).
function isDisconnected( node ) {
	return !node || !node.parentNode || node.parentNode.nodeType === 11;
}

teabo.each({
	parent: function( elem ) {
		var parent = elem.parentNode;
		return parent && parent.nodeType !== 11 ? parent : null;
	},
	parents: function( elem ) {
		return teabo.dir( elem, "parentNode" );
	},
	parentsUntil: function( elem, i, until ) {
		return teabo.dir( elem, "parentNode", until );
	},
	next: function( elem ) {
		return teabo.nth( elem, 2, "nextSibling" );
	},
	prev: function( elem ) {
		return teabo.nth( elem, 2, "previousSibling" );
	},
	nextAll: function( elem ) {
		return teabo.dir( elem, "nextSibling" );
	},
	prevAll: function( elem ) {
		return teabo.dir( elem, "previousSibling" );
	},
	nextUntil: function( elem, i, until ) {
		return teabo.dir( elem, "nextSibling", until );
	},
	prevUntil: function( elem, i, until ) {
		return teabo.dir( elem, "previousSibling", until );
	},
	siblings: function( elem ) {
		return teabo.sibling( ( elem.parentNode || {} ).firstChild, elem );
	},
	children: function( elem ) {
		return teabo.sibling( elem.firstChild );
	},
	contents: function( elem ) {
		return teabo.nodeName( elem, "iframe" ) ?
				elem.contentDocument || elem.contentWindow.document :
					teabo.makeArray( elem.childNodes );
	}
}, function( name, fn ) {
	teabo.fn[ name ] = function( until, selector ) {
		var ret = teabo.map( this, fn, until );

		if ( !runtil.test( name ) ) {
			selector = until;
		}

		if ( selector && typeof selector === "string" ) {
			ret = teabo.filter( selector, ret );
		}

		ret = this.length > 1 && !guaranteedUnique[ name ] ? teabo.unique( ret ) : ret;

		if ( (this.length > 1 || rmultiselector.test( selector )) && rparentsprev.test( name ) ) {
			ret = ret.reverse();
		}

		return this.pushStack( ret, name, slice.call( arguments ).join(",") );
	};
});

teabo.extend({
	filter: function( expr, elems, not ) {
		if ( not ) {
			expr = ":not(" + expr + ")";
		}

		return elems.length === 1 ?
				teabo.find.matchesSelector(elems[0], expr) ? [ elems[0] ] : [] :
					teabo.find.matches(expr, elems);
	},

	dir: function( elem, dir, until ) {
		var matched = [],
		cur = elem[ dir ];

		while ( cur && cur.nodeType !== 9 && (until === undefined || cur.nodeType !== 1 || !teabo( cur ).is( until )) ) {
			if ( cur.nodeType === 1 ) {
				matched.push( cur );
			}
			cur = cur[dir];
		}
		return matched;
	},

	nth: function( cur, result, dir, elem ) {
		result = result || 1;
		var num = 0;

		for ( ; cur; cur = cur[dir] ) {
			if ( cur.nodeType === 1 && ++num === result ) {
				break;
			}
		}

		return cur;
	},

	sibling: function( n, elem ) {
		var r = [];

		for ( ; n; n = n.nextSibling ) {
			if ( n.nodeType === 1 && n !== elem ) {
				r.push( n );
			}
		}

		return r;
	}
});

	var ralpha = /alpha\([^)]*\)/i,
		ropacity = /opacity=([^)]*)/,
		// fixed for IE9, see #8346
		rupper = /([A-Z]|^ms)/g,
		rnum = /^[\-+]?(?:\d*\.)?\d+$/i,
		rnumnonpx = /^-?(?:\d*\.)?\d+(?!px)[^\d\s]+$/i,
		rrelNum = /^([\-+])=([\-+.\de]+)/,
		rmargin = /^margin/,

		cssShow = { position: "absolute", visibility: "hidden", display: "block" },

		// order is important!
		cssExpand = [ "Top", "Right", "Bottom", "Left" ],

		curCSS,

		getComputedStyle,
		currentStyle;

	teabo.fn.css = function( name, value ) {
		return teabo.access( this, function( elem, name, value ) {
			return value !== undefined ?
				teabo.style( elem, name, value ) :
				teabo.css( elem, name );
		}, name, value, arguments.length > 1 );
	};

	teabo.extend({
		// Add in style property hooks for overriding the default
		// behavior of getting and setting a style property
		cssHooks: {
			opacity: {
				get: function( elem, computed ) {
					if ( computed ) {
						// We should always get a number back from opacity
						var ret = curCSS( elem, "opacity" );
						return ret === "" ? "1" : ret;

					} else {
						return elem.style.opacity;
					}
				}
			}
		},

		// Exclude the following css properties to add px
		cssNumber: {
			"fillOpacity": true,
			"fontWeight": true,
			"lineHeight": true,
			"opacity": true,
			"orphans": true,
			"widows": true,
			"zIndex": true,
			"zoom": true
		},

		// Add in properties whose names you wish to fix before
		// setting or getting the value
		cssProps: {
			// normalize float css property
			"float": teabo.support.cssFloat ? "cssFloat" : "styleFloat"
		},

		// Get and set the style property on a DOM Node
		style: function( elem, name, value, extra ) {
			// Don't set styles on text and comment nodes
			if ( !elem || elem.nodeType === 3 || elem.nodeType === 8 || !elem.style ) {
				return;
			}

			// Make sure that we're working with the right name
			var ret, type, origName = teabo.camelCase( name ),
				style = elem.style, hooks = teabo.cssHooks[ origName ];

			name = teabo.cssProps[ origName ] || origName;

			// Check if we're setting a value
			if ( value !== undefined ) {
				type = typeof value;

				// convert relative number strings (+= or -=) to relative numbers. #7345
				if ( type === "string" && (ret = rrelNum.exec( value )) ) {
					value = ( +( ret[1] + 1) * +ret[2] ) + parseFloat( teabo.css( elem, name ) );
					// Fixes bug #9237
					type = "number";
				}

				// Make sure that NaN and null values aren't set. See: #7116
				if ( value == null || type === "number" && isNaN( value ) ) {
					return;
				}

				// If a number was passed in, add 'px' to the (except for certain CSS properties)
				if ( type === "number" && !teabo.cssNumber[ origName ] ) {
					value += "px";
				}

				// If a hook was provided, use that value, otherwise just set the specified value
				if ( !hooks || !("set" in hooks) || (value = hooks.set( elem, value )) !== undefined ) {
					// Wrapped to prevent IE from throwing errors when 'invalid' values are provided
					// Fixes bug #5509
					try {
						style[ name ] = value;
					} catch(e) {}
				}

			} else {
				// If a hook was provided get the non-computed value from there
				if ( hooks && "get" in hooks && (ret = hooks.get( elem, false, extra )) !== undefined ) {
					return ret;
				}

				// Otherwise just get the value from the style object
				return style[ name ];
			}
		},

		css: function( elem, name, extra ) {
			var ret, hooks;

			// Make sure that we're working with the right name
			name = teabo.camelCase( name );
			hooks = teabo.cssHooks[ name ];
			name = teabo.cssProps[ name ] || name;

			// cssFloat needs a special treatment
			if ( name === "cssFloat" ) {
				name = "float";
			}

			// If a hook was provided get the computed value from there
			if ( hooks && "get" in hooks && (ret = hooks.get( elem, true, extra )) !== undefined ) {
				return ret;

			// Otherwise, if a way to get the computed value exists, use that
			} else if ( curCSS ) {
				return curCSS( elem, name );
			}
		},

		// A method for quickly swapping in/out CSS properties to get correct calculations
		swap: function( elem, options, callback ) {
			var old = {},
				ret, name;

			// Remember the old values, and insert the new ones
			for ( name in options ) {
				old[ name ] = elem.style[ name ];
				elem.style[ name ] = options[ name ];
			}

			ret = callback.call( elem );

			// Revert the old values
			for ( name in options ) {
				elem.style[ name ] = old[ name ];
			}

			return ret;
		}
	});

	// DEPRECATED in 1.3, Use teabo.css() instead
	teabo.curCSS = teabo.css;

	if ( document.defaultView && document.defaultView.getComputedStyle ) {
		getComputedStyle = function( elem, name ) {
			var ret, defaultView, computedStyle, width,
				style = elem.style;

			name = name.replace( rupper, "-$1" ).toLowerCase();

			if ( (defaultView = elem.ownerDocument.defaultView) &&
					(computedStyle = defaultView.getComputedStyle( elem, null )) ) {

				ret = computedStyle.getPropertyValue( name );
				if ( ret === "" && !teabo.contains( elem.ownerDocument.documentElement, elem ) ) {
					ret = teabo.style( elem, name );
				}
			}

			// A tribute to the "awesome hack by Dean Edwards"
			// WebKit uses "computed value (percentage if specified)" instead of "used value" for margins
			// which is against the CSSOM draft spec: http://dev.w3.org/csswg/cssom/#resolved-values
			if ( !teabo.support.pixelMargin && computedStyle && rmargin.test( name ) && rnumnonpx.test( ret ) ) {
				width = style.width;
				style.width = ret;
				ret = computedStyle.width;
				style.width = width;
			}

			return ret;
		};
	}

	if ( document.documentElement.currentStyle ) {
		currentStyle = function( elem, name ) {
			var left, rsLeft, uncomputed,
				ret = elem.currentStyle && elem.currentStyle[ name ],
				style = elem.style;

			// Avoid setting ret to empty string here
			// so we don't default to auto
			if ( ret == null && style && (uncomputed = style[ name ]) ) {
				ret = uncomputed;
			}

			// From the awesome hack by Dean Edwards
			// http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291

			// If we're not dealing with a regular pixel number
			// but a number that has a weird ending, we need to convert it to pixels
			if ( rnumnonpx.test( ret ) ) {

				// Remember the original values
				left = style.left;
				rsLeft = elem.runtimeStyle && elem.runtimeStyle.left;

				// Put in the new values to get a computed value out
				if ( rsLeft ) {
					elem.runtimeStyle.left = elem.currentStyle.left;
				}
				style.left = name === "fontSize" ? "1em" : ret;
				ret = style.pixelLeft + "px";

				// Revert the changed values
				style.left = left;
				if ( rsLeft ) {
					elem.runtimeStyle.left = rsLeft;
				}
			}

			return ret === "" ? "auto" : ret;
		};
	}

	curCSS = getComputedStyle || currentStyle;

	function getWidthOrHeight( elem, name, extra ) {

		// Start with offset property
		var val = name === "width" ? elem.offsetWidth : elem.offsetHeight,
			i = name === "width" ? 1 : 0,
			len = 4;

		if ( val > 0 ) {
			if ( extra !== "border" ) {
				for ( ; i < len; i += 2 ) {
					if ( !extra ) {
						val -= parseFloat( teabo.css( elem, "padding" + cssExpand[ i ] ) ) || 0;
					}
					if ( extra === "margin" ) {
						val += parseFloat( teabo.css( elem, extra + cssExpand[ i ] ) ) || 0;
					} else {
						val -= parseFloat( teabo.css( elem, "border" + cssExpand[ i ] + "Width" ) ) || 0;
					}
				}
			}

			return val + "px";
		}

		// Fall back to computed then uncomputed css if necessary
		val = curCSS( elem, name );
		if ( val < 0 || val == null ) {
			val = elem.style[ name ];
		}

		// Computed unit is not pixels. Stop here and return.
		if ( rnumnonpx.test(val) ) {
			return val;
		}

		// Normalize "", auto, and prepare for extra
		val = parseFloat( val ) || 0;

		// Add padding, border, margin
		if ( extra ) {
			for ( ; i < len; i += 2 ) {
				val += parseFloat( teabo.css( elem, "padding" + cssExpand[ i ] ) ) || 0;
				if ( extra !== "padding" ) {
					val += parseFloat( teabo.css( elem, "border" + cssExpand[ i ] + "Width" ) ) || 0;
				}
				if ( extra === "margin" ) {
					val += parseFloat( teabo.css( elem, extra + cssExpand[ i ]) ) || 0;
				}
			}
		}

		return val + "px";
	}

	teabo.each([ "height", "width" ], function( i, name ) {
		teabo.cssHooks[ name ] = {
			get: function( elem, computed, extra ) {
				if ( computed ) {
					if ( elem.offsetWidth !== 0 ) {
						return getWidthOrHeight( elem, name, extra );
					} else {
						return teabo.swap( elem, cssShow, function() {
							return getWidthOrHeight( elem, name, extra );
						});
					}
				}
			},

			set: function( elem, value ) {
				return rnum.test( value ) ?
					value + "px" :
					value;
			}
		};
	});

	if ( !teabo.support.opacity ) {
		teabo.cssHooks.opacity = {
			get: function( elem, computed ) {
				// IE uses filters for opacity
				return ropacity.test( (computed && elem.currentStyle ? elem.currentStyle.filter : elem.style.filter) || "" ) ?
					( parseFloat( RegExp.$1 ) / 100 ) + "" :
					computed ? "1" : "";
			},

			set: function( elem, value ) {
				var style = elem.style,
					currentStyle = elem.currentStyle,
					opacity = teabo.isNumeric( value ) ? "alpha(opacity=" + value * 100 + ")" : "",
					filter = currentStyle && currentStyle.filter || style.filter || "";

				// IE has trouble with opacity if it does not have layout
				// Force it by setting the zoom level
				style.zoom = 1;

				// if setting opacity to 1, and no other filters exist - attempt to remove filter attribute #6652
				if ( value >= 1 && teabo.trim( filter.replace( ralpha, "" ) ) === "" ) {

					// Setting style.filter to null, "" & " " still leave "filter:" in the cssText
					// if "filter:" is present at all, clearType is disabled, we want to avoid this
					// style.removeAttribute is IE Only, but so apparently is this code path...
					style.removeAttribute( "filter" );

					// if there there is no filter style applied in a css rule, we are done
					if ( currentStyle && !currentStyle.filter ) {
						return;
					}
				}

				// otherwise, set new filter values
				style.filter = ralpha.test( filter ) ?
					filter.replace( ralpha, opacity ) :
					filter + " " + opacity;
			}
		};
	}
	
	teabo(function() {
		// This hook cannot be added until DOM ready because the support test
		// for it is not run until after DOM ready
		if ( !teabo.support.reliableMarginRight ) {
			teabo.cssHooks.marginRight = {
				get: function( elem, computed ) {
					// WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
					// Work around by temporarily setting element display to inline-block
					return teabo.swap( elem, { "display": "inline-block" }, function() {
						if ( computed ) {
							return curCSS( elem, "margin-right" );
						} else {
							return elem.style.marginRight;
						}
					});
				}
			};
		}
	});

	if ( teabo.expr && teabo.expr.filters ) {
		teabo.expr.filters.hidden = function( elem ) {
			var width = elem.offsetWidth,
				height = elem.offsetHeight;

			return ( width === 0 && height === 0 ) || (!teabo.support.reliableHiddenOffsets && ((elem.style && elem.style.display) || teabo.css( elem, "display" )) === "none");
		};

		teabo.expr.filters.visible = function( elem ) {
			return !teabo.expr.filters.hidden( elem );
		};
	}

	// These hooks are used by animate to expand properties
	teabo.each({
		margin: "",
		padding: "",
		border: "Width"
	}, function( prefix, suffix ) {

		teabo.cssHooks[ prefix + suffix ] = {
			expand: function( value ) {
				var i,

					// assumes a single number if not a string
					parts = typeof value === "string" ? value.split(" ") : [ value ],
					expanded = {};

				for ( i = 0; i < 4; i++ ) {
					expanded[ prefix + cssExpand[ i ] + suffix ] =
						parts[ i ] || parts[ i - 2 ] || parts[ 0 ];
				}

				return expanded;
			}
		};
	});
	
	
	// Create width, height, innerHeight, innerWidth, outerHeight and outerWidth methods
	teabo.each( { Height: "height", Width: "width" }, function( name, type ) {
		var clientProp = "client" + name,
			scrollProp = "scroll" + name,
			offsetProp = "offset" + name;

		// innerHeight and innerWidth
		teabo.fn[ "inner" + name ] = function() {
			var elem = this[0];
			return elem ?
				elem.style ?
				parseFloat( teabo.css( elem, type, "padding" ) ) :
				this[ type ]() :
				null;
		};

		// outerHeight and outerWidth
		teabo.fn[ "outer" + name ] = function( margin ) {
			var elem = this[0];
			return elem ?
				elem.style ?
				parseFloat( teabo.css( elem, type, margin ? "margin" : "border" ) ) :
				this[ type ]() :
				null;
		};

		teabo.fn[ type ] = function( value ) {
			return teabo.access( this, function( elem, type, value ) {
				var doc, docElemProp, orig, ret;
				if ( teabo.isWindow( elem ) ) {
					// 3rd condition allows Nokia support, as it supports the docElem prop but not CSS1Compat
					doc = elem.document;
					docElemProp = doc.documentElement[ clientProp ];
					return teabo.support.boxModel && docElemProp ||
						doc.body && doc.body[ clientProp ] || docElemProp;
				}

				// Get document width or height
				if ( elem.nodeType === 9 ) {
					// Either scroll[Width/Height] or offset[Width/Height], whichever is greater
					doc = elem.documentElement;

					// when a window > document, IE6 reports a offset[Width/Height] > client[Width/Height]
					// so we can't use max, as it'll choose the incorrect offset[Width/Height]
					// instead we use the correct client[Width/Height]
					// support:IE6
					if ( doc[ clientProp ] >= doc[ scrollProp ] ) {
						return doc[ clientProp ];
					}
					return Math.max(
						elem.body[ scrollProp ], doc[ scrollProp ],
						elem.body[ offsetProp ], doc[ offsetProp ]
					);
				}

				// Get width or height on the element
				if ( value === undefined ) {
					orig = teabo.css( elem, type );
					ret = parseFloat( orig );
					return teabo.isNumeric( ret ) ? ret : orig;
				}

				// Set the width or height on the element
				teabo( elem ).css( type, value );
			}, type, value, arguments.length, null );
		};
	});
	
window.teabo = window.$ = teabo;
// Expose teabo as an AMD module, but only for AMD loaders that
// understand the issues with loading multiple versions of teabo
// in a page that all might call define(). The loader will indicate
// they have special allowances for multiple teabo versions by
// specifying define.amd.teabo = true. Register as a named module,
// since teabo can be concatenated with other files that may use define,
// but not use a proper concatenation script that understands anonymous
// AMD modules. A named AMD is safest and most robust way to register.
// Lowercase teabo is used because AMD module names are derived from
// file names, and teabo is normally delivered in a lowercase file name.
// Do this after creating the global so that if an AMD module wants to call
// noConflict to hide this version of teabo, it will work.
if ( typeof define === "function" && define.amd && define.amd.teabo ) {
	define( "teabo", [], function () { return teabo; } );
}
})( window );
