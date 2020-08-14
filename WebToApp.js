class WebToApp
	{
	constructor(data, fileName, mimeType)
		{
		// SETTING THE RECEIVED VALUES
		this.data = data;
		this.fileName = fileName;
		this.mimeType = mimeType;

		// CHECKING THE DATA TYPE
		if (typeof this.data === "string" || this.data instanceof String)
			{
			// CONVERTING THE STRING TO AN ARRAYBUFFER BEFORE DOWNLOADING
			this.stringToArrayBuffer(this.data, "UTF-8", this.download);
			}
			else
			{
			// DOWNLOADING THE FILE
			this.download();
			}
		}

	download()
		{
		try
			{
			// CREATING THE OBJECT WITH THE DATA FOR THE IOS OR OS X NATIVE APP
			var WEBTOAPP_DATA = {filename: this.fileName, fileContent: this.arrayBufferToBase64(this.data)};

			// SENDING THE OBJECT TO THE IOS OR OS X NATIVE APP
			window.webkit.messageHandlers.webToApp.postMessage(WEBTOAPP_DATA);
			}
			catch(err)
			{
			// SENDING THE FILENAME TO THE ANDROID NATIVE APP
			console.log("WEBTOAPP_FILENAME=" + this.fileName);

			// SENDING THE DATA TO THE ANDROID NATIVE APP OR A WEB BROWSER
			var blob = new Blob([this.data], {type: this.mimeType});
			var url = window.URL.createObjectURL(blob);
			this.download_URL(url);
			}
		}

	download_URL(data)
		{
		var a;
		a = document.createElement("a");
		a.href = data;
		a.download = this.fileName;
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

	stringToArrayBuffer(string, encoding, callback)
		{
		var _WebToApp = this;
		var blob = new Blob([string], {type:"text/plain;charset=" + encoding});
		var reader = new FileReader();
		reader.onload = function(event)
			{
			_WebToApp.data = event.target.result;
			_WebToApp.download();
			};
		reader.readAsArrayBuffer(blob);
		}
	}