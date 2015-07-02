const _GWTVIZ_DIV_IFRAME = 'gwtviz_div_iframe';
const _GWTVIZ_IFRAME = 'gwtviz_iframe';
const _GWTVIZ_MOUNT_POINT_DEV = 'gwtviz-mount-point-dev';
const _GWTVIZ_MAIN_PANEL_EDIT = "gwtviz-main-panel-edit";
const _DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE = 'edit-field-visual-space';
const _DRUPAL_EDIT__CONTENT_CLEARFIX = 'content clearfix'; // This class should be
														// hidden
const _DRUPAL_EDIT_BLOCK_SYSTEM_MAIN = 'block-system-main';
const _DRUPAL_EDIT_FIELD_NAME_FIELD_VISUAL_SPACE = "field field-name-field-visual-space field-type-text-long field-label-above";

window.onload = function gwtvizMount() {

	mount();
	function mount() {
		findRelativePath();
		// If this is the second time in DEV page this mount point should be
		// here.
		var devMountPoint = findExistingGWTVIZDevMountPoint();
		if (devMountPoint == null) {
			// Lets see if were are in DEV page here
			devMountPoint = findExistingDrupalDevMountPointID();
		}

		// If we have a DEV Mount point lets add our IFrame
		if (devMountPoint != null) {
			var parentElement = devMountPoint.parentElement;
			parentElement.id = _GWTVIZ_MOUNT_POINT_DEV;
			document.getElementById(_GWTVIZ_MOUNT_POINT_DEV).appendChild(makeDevTimeIFrame('Design'));
			// document.getElementById(_GWTVIZ_IFRAME).setAttribute('onload',setIframeHeight(_GWTVIZ_IFRAME));
			setIframeHeight(_GWTVIZ_IFRAME);
			
			if (devMountPoint != null) {
				devMountPoint.style.display = "none";
			}
			//Resize the TextArea so that the controls on the right side do not crash into GWTViz
			var editArea = document.getElementById('edit-body-und-0-value_ifr');
			if(editArea!=null){
				editArea.style.height='425px'
			}
			return;
		}

		// OK, not DEV mount point.
		// Lets test for RUNTIME mount point
		var runtimeMountPoint = findExistingDrupalRuntimeMount();
		if (runtimeMountPoint != null) {
			// We have the runtimeMountPoint;
			document.getElementById(_GWTVIZ_MAIN_PANEL_EDIT).appendChild(makeDevTimeIFrame('Runtime'));
			// document.getElementById(_GWTVIZ_IFRAME).setAttribute('onload'
			// ,setIframeHeight(_GWTVIZ_IFRAME));
			setIframeHeight(_GWTVIZ_IFRAME);

			var elementsToHide = document.getElementById(
					_GWTVIZ_MAIN_PANEL_EDIT).getElementsByClassName(
					_DRUPAL_EDIT_FIELD_NAME_FIELD_VISUAL_SPACE);
			if (elementsToHide != null && elementsToHide.length != null
					&& elementsToHide.length > 0) {
				elementsToHide[0].style.display = "none";
			}
		}

	}

	// function makeDevTimeIFrame(){
	// var urlPath = findRelativePath();
	// console.log('Current Location->'+urlPath);
	// // <iframe src="http://www.w3schools.com"></iframe>
	// var iframe = document.createElement("iframe");
	// iframe.src = urlPath +'/index2.html';
	// iframe.height = "800px";
	// iframe.width = "800px";
	// return iframe;
	// }

	function makeDevTimeIFrame(type) {

		var urlPath = findRelativePath();
		console.log('Current Location->' + urlPath);

		
		var divIFrame = document.createElement("div");
		divIFrame.setAttribute('id', _GWTVIZ_DIV_IFRAME);
		divIFrame.style.position = 'relative';
		//divIFrame.style.left = '2%';
		//divIFrame.style.right = '2%';
		//divIFrame.style.width = window.innerWidth+'px';
		divIFrame.style.width = '100%';
		divIFrame.style.visibility = 'visible';

		var _navBar = document.getElementsByClassName('navbar-lining clearfix');
		console.log('found navbar-lining clearfix '+_navBar);
		var _navBarHorizontal = document.getElementsByClassName('navbar-tray overlay-displace-top active navbar-tray-horizontal');
		if(_navBarHorizontal.length>0){
			var _width = (window.innerWidth - (window.innerWidth - window.document.documentElement.getElementsByTagName('body')[0].clientWidth))-(window.innerWidth *0.20);
			console.log('found top navbar-lining clearfix '+_width);
			divIFrame.style.width = _width+'px';
		}else{
			console.log('found left navbar-lining clearfix '+window.innerWidth);
			var _width =  (window.innerWidth - (window.innerWidth - window.document.documentElement.getElementsByTagName('body')[0].clientWidth))-(window.innerWidth *0.15);
			divIFrame.style.width = _width+'px';
		}
		divIFrame.style.width = '100%';
				
		var iframe = document.createElement("iframe");
		iframe.setAttribute('id', _GWTVIZ_IFRAME);
		iframe.setAttribute('src', urlPath + 'viz.html?' + type); // change the URL
		iframe.setAttribute('width', '100%');
		iframe.setAttribute('height', '1480px');
		iframe.setAttribute('frameBorder', '0');
		iframe.setAttribute('scrolling', 'no');
		iframe.style.visibility = 'hidden';
		iframe.style.visibility = 'visible';
		
		
		divIFrame.appendChild(iframe);
		// iframe.setAttribute('onload' ,setIframeHeight(iframe.id));
		return divIFrame;

	}

	function replaceContentInContainer(matchClass, content) {
		var elems = document.getElementsByTagName('*'), i;
		for (i in elems) {
			if ((' ' + elems[i].className + ' ')
					.indexOf(' ' + matchClass + ' ') > -1) {
				elems[i].innerHTML = content;
			}
		}
	}

	// Returns Node
	function findExistingGWTVIZDevMountPoint() {
		var elems = document.getElementsByTagName('*'), i;
		for (i in elems) {
			if ((' ' + elems[i].className + ' ').indexOf(' '
					+ _GWTVIZ_MOUNT_POINT_DEV + ' ') > -1) {
				return elems[i];
			}
		}
		return null;
	}

	// Returns Node
	function findExistingDrupalDevMountPoint() {
		var elems = document.getElementsByTagName('*'), i;
		for (i in elems) {
			if ((' ' + elems[i].className + ' ').indexOf(' '
					+ _DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE + ' ') > -1) {
				return elems[i];
			}
		}
		return null;
	}
	// Returns HTMLElement
	function findExistingDrupalDevMountPointID() {
		var elem = document
				.getElementById(_DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE), i;
		if (elem == null) {
			return null;
		} else {
			return elem;
		}

	}
	// document.getElementById('main').getElementsByClassName('test');

	// Sets up the RUNTIME Mount point and tags it with _GWTVIZ_MAIN_PANEL_EDIT
	function findExistingDrupalRuntimeMount() {
		var contentClearFix = document.getElementById(
				_DRUPAL_EDIT_BLOCK_SYSTEM_MAIN).getElementsByClassName(
				_DRUPAL_EDIT__CONTENT_CLEARFIX);
		if (contentClearFix != null && contentClearFix.length != null && contentClearFix.length > 0) {
			var editContentDiv = contentClearFix[0];
			editContentDiv.id = _GWTVIZ_MAIN_PANEL_EDIT;
			return editContentDiv;
		}
		return null;
	}
	

	function findRelativePath() {
		var scripts = document.getElementsByTagName('script');
		var scriptsCount = scripts.length;
		for (i = 0; i < scriptsCount; i++) {
			var data = scripts[i].src;
			var pos = data.indexOf('js/itgp/itgpMount.js');
			if (pos > 0) {
				return data.substring(0, pos);
			}
		}
		return null;
	}

	// http://www.christersvensson.com/html-tool/iframe.htm
	function getDocHeight(doc) {
		doc = doc || document;
		// from
		// http://stackoverflow.com/questions/1145850/get-height-of-entire-document-with-javascript
		var body = doc.body, html = doc.documentElement;
		var height = Math.max(body.scrollHeight, body.offsetHeight,
				html.clientHeight, html.scrollHeight, html.offsetHeight);
		return height;
	}
	function setIframeHeight(id) {
		var parentWindow = window.top;

		// var ifrm = parentWindow.document.getElementById(id);
		// var doc = ifrm.contentDocument? ifrm.contentDocument:
		// ifrm.contentWindow.document;
		// ifrm.style.visibility = 'hidden';
		// ifrm.style.height = "10px"; // reset to minimal height in case going
		// from longer to shorter doc
		// ifrm.style.height = getDocHeight( doc ) + 10 + "px";
		// ifrm.style.visibility = 'visible';

		var w = parent.window, d = document, e = d.documentElement, g = d
				.getElementsByTagName('body')[0], x = w.innerWidth
				|| e.clientWidth || g.clientWidth, y = w.innerHeight
				|| e.clientHeight || g.clientHeight;
		console.log("X:" + x + "  Y:" + y);

	}

}