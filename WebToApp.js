class WebToApp
	{
	constructor(data, fileName, mimeType)
		{
		try
			{
			// Creating the object with the data for the iOS or OS X native App
			var WEBTOAPP_DATA = {filename: fileName, fileContent: this.arrayBufferToBase64(data)};

			// Sending the object to the iOS or OS X native App
			window.webkit.messageHandlers.webToApp.postMessage(WEBTOAPP_DATA);
			}
			catch(err)
			{
			// Sending the filename to the Android native App
			console.log("WEBTOAPP_FILENAME=" + fileName);

			// Sending the data to the Android native App or a Web browser
			var blob = new Blob([data], {type: mimeType});
			var url = window.URL.createObjectURL(blob);
			this.download_URL(url, fileName);
			}
		}

	download_URL(data, fileName)
		{
		var a;
		a = document.createElement("a");
		a.href = data;
		a.download = fileName;
		document.body.appendChild(a);
		a.style = "display: none";
		a.click();
		a.remove();
		}

	arrayBufferToBase64(buffer)
		{
		var binary = "";
		var bytes = new Uint8Array(buffer);
		var len = bytes.byteLength;
		for (var i = 0; i < len; i++)
			{
			binary += String.fromCharCode(bytes[i]);
			}
			return window.btoa(binary);
		}
	}