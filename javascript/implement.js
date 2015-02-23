window.onload = function(){//this is where all the posts are created
createCategory("Web Development");
createPost("Brunosilutions.com redesign", "../img/bruno-preview.png", "http://www.silvertale.webuda.com", "I redesigned this website to have a sleaker, cleaner look and feel. The origenal website was outdated, but the content was still good. I extracted and preserved the written content with a few enhancements, but redesigned the styling and background mechanics of the site using jquery and bootstrap for an organized, fast website. ");  
createCategory("java");
createPost("Conway's game of life", "../img/Conway.png", "https://github.com/Silvertale/Conway-s-Game-Of-Life.git", "This is an improved version of \"Conway's Game of Life\" which adds variable simulation speed, variable universe size, an organism and generation counter, as well as the ability to export the current generation as an image file. This was created along with several other artificial life simulations to study how just a few basic rules can create a structured organic simulation that appears natural.");
createCategory("Contact");
createContact("Please Contact Me Via My Contact Form Below", "Send...");
}
