var path =
	"ws://" + 
	location.hostname + 
	":" + location.port + "/socket";

var load = document.createElement('div');
load.classList.add("load");

//not in use
function getScrollPosition()
{
  var position  = window.pageYOffset || document.documentElement.scrollTop
  return position;
}

//not in use
function setScrollPosition(y)
{
    window.scrollTo(0, y);
}

function update(content)
{
	document.body.innerHTML = "";
	document.body.appendChild(content);
	document.body.removeAttribute("style");
}

update(
	load
);

var socket = null;
try {
	socket = new WebSocket(path);
}
catch(err) {
	window.location.reload();
}

socket.onopen = function(event)
{
    socket.send("path:" + window.location.pathname);
}

socket.onerror = function(event)
{
	update(
		load
	);
};

socket.onclose = function(event)
{
	update(
		load
	);
	window.location.reload();
};

socket.onmessage = function(event)
{
	var msg = event.data;
	var convertedMsg = msg.split("&&", 4);
	var name = convertedMsg[0].replace("name:", "");
	var style = convertedMsg[1].replace("style:", "");
	var bootstrap = convertedMsg[2].replace("classes:", "");
	var hypertext = convertedMsg[3].replace("hypertext:", "");
	var content = document.body;
	document.title = name;
	content.setAttribute("style", style);
	content.setAttribute("class", bootstrap);
	content.innerHTML = hypertext;
	const buttons = document.getElementsByTagName('button');
	for (const button of buttons)
	{
		button.addEventListener('click', function()
		{
		    update(
        		load
        	);
			socket.send("id:" + button.id + " inputs:{}");
		});
	}
    const forms = document.getElementsByTagName('form');
    for (const form of forms)
    {
        form.addEventListener('submit', function(e)
        {
            update(
                load
            );
            const inputs = form.getElementsByTagName('input');
            var inputsAsString = "";
            for (const input of inputs)
            {
                inputsAsString += input.id + "??" + input.value + ";;";
            }
            var inputsAsString = "inputs:{" + inputsAsString + "}";
            socket.send("id:" + form.id + " " + inputsAsString);
        });
    }
};
