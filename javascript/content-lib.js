

document.getElementById('contact').onclick = function(e){
//add contact form to main content and scroll there
    $('html, body').scrollTop( $(document).height() );
}


function createPost(title, imgURL, imgLink, discription){
var container = $("#mainContent");
    
var panel = document.createElement("div");
panel.className = "panel panel-default";
var heading = document.createElement("div");
    heading.className = "panel-heading post-title-text";
var headingText = document.createTextNode(title);
var panelContent = document.createElement("div");
    panelContent.className = "panel-content content-padding";
var table = document.createElement("table");
    table.style = "width: 100%";
var row = document.createElement("tr");
var td1 = document.createElement("td");
    
    
if(imgURL.trim() != ""){//keep from creating an image if there isn't one to display
    var link = document.createElement("a");
    if(imgLink.trim() != ""){
    link.setAttribute('href', imgLink);   
    }else{
    link.setAttribute('htrf', '#');   
    }
    var img = document.createElement("img");
        img.className = "thumbnail";
        img.src = imgURL;
        img.onmouseover = function(){
        img.className = "large-thumbnail";   
        }
        img.onmouseout = function(){
        img.className = "thumbnail";   
        }
}
    
    
var td2 = document.createElement("td");
var td2Text = document.createTextNode(discription);

if(imgURL.trim() != ""){//do not append an image if there isn't one to display
link.appendChild(img);
td1.appendChild(link);
}
td2.appendChild(td2Text);
row.appendChild(td1);
row.appendChild(td2);
table.appendChild(row);
panelContent.appendChild(table);

heading.appendChild(headingText);
panel.appendChild(heading);
panel.appendChild(panelContent);
container.append(panel);
}


function createCategory(title){//adds a category
var container = $("#mainContent");
var space = document.createElement("div");
    space.className = "v-space-m";
var heading = document.createElement("div");
    heading.className = "panel panel-default heading-text-1 text-center";
var text = document.createTextNode(title);

heading.appendChild(text);
container.append(space);
container.append(heading); 
}


function createContact(title, submitText){
var content = $("#mainContent");
    
var panel = document.createElement("div");
    panel.className = "panel panel-default text-center";
var heading = document.createElement("div");
    heading.className = "panel-heading";
var headingText = document.createElement("h3");
    headingText.className = "panel-title post-title-text";
    headingText.appendChild(document.createTextNode(title));
var body = document.createElement("div");
    body.className = "panel-body";
    
var form = document.createElement("form");
    form.setAttribute("action", "../php/sendmail.php");
    form.setAttribute("method", "POST");
var alert = document.createElement("div");
    alert.className = "alert alert-info toggle-invisible";
var alertClose = document.createElement("div");
    //alertClose.setAttribute("href", "#");
    //alertClose.className = "toggle-invisible";
var email = document.createElement("input");
    email.setAttribute("type", "text");
    email.className = "form-control";
    email.setAttribute("placeholder", "Your Email");
    email.setAttribute("name", "email");
var spacer1 = document.createElement("div");
    spacer1.className = "v-space-s";
var subject = document.createElement("input");
    subject.setAttribute("type", "text");
    subject.className = "form-control";
    subject.setAttribute("placeholder", "Subject");
    subject.setAttribute("name", "subject");
var spacer2 = document.createElement("div");
    spacer2.className = "v-space-s";
var message = document.createElement("textarea");
    message.setAttribute("rows", "5");
    message.setAttribute("cols", "70");
    message.className = "form-control";
    message.setAttribute("placeholder", "Message");
    message.setAttribute("name", "message");
    message.style.resize = "vertical";
var spacer3 = document.createElement("div");
    spacer3.className = "v-space-s";
var submit = document.createElement("submit");
    submit.className = "btn btn-default";
    submit.appendChild(document.createTextNode(submitText));
    submit.onclick = function(e){//submit form using javascript
        
        
        $.ajax({
            type: "POST",
            url: '../php/sendmail.php',
            data: { email: email.value, subject: subject.value, message: message.value },
            success: function(data) {
            alert.classList.remove("toggle-invisible");
            alert.classList.add("toggle-visible");
            email.value = "";
            subject.value = "";
            message.value = "";
            },
            fail: function(data) {
            // just in case posting your form failed
            alert( "Posting failed. Did you fill all the required fields?" );
            }
        });

        return false;
 
        }
    
    alert.onclick = function(){
    alert.classList.remove("toggle-visible");
    alert.classList.add("toggle-invisible");
    }
        
        
        
        
        
    

alertClose.appendChild(document.createTextNode("Your Email has been sent!"));    
alert.appendChild(alertClose);
form.appendChild(alert);                       
form.appendChild(email);
form.appendChild(spacer1);
form.appendChild(subject);
form.appendChild(spacer2);
form.appendChild(message);
form.appendChild(spacer3);
form.appendChild(submit);

body.appendChild(form);
heading.appendChild(headingText);
panel.appendChild(heading);
panel.appendChild(body);

content.append(panel);
    
    
    

}