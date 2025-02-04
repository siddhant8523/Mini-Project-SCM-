console.log("Script Loaded");

// Changing The Website Theme 

let currentTheme=getTheme();

document.addEventListener("DOMContentLoaded",()=>{
    changeTheme();
    });

function changeTheme(){
    changePageTheme(currentTheme,currentTheme)
    const changeThemeButton=document.querySelector("#theme_change_button");

    changeThemeButton.addEventListener("click",(event)=>{
        let oldTheme=currentTheme;
        console.log("Theme change on clicking");
        if(currentTheme=="dark"){
            currentTheme="light";
        }
        else{
            currentTheme="dark";
        }
        changePageTheme(currentTheme,oldTheme);
    });


}

function setTheme(theme){
    localStorage.setItem("theme",theme);
}

function getTheme(){
    let theme=localStorage.getItem("theme");
    return theme? theme:"light";
}

function changePageTheme(theme,oldTheme){
    setTheme(currentTheme);
    document.querySelector("html").classList.remove(oldTheme);
    document.querySelector("html").classList.add(currentTheme);

    document.querySelector("#theme_change_button").querySelector("span").textContent=currentTheme=="light"?"dark":"light";
}

//Ending the work of changing Theme 