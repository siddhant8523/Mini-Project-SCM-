console.log("Script Loaded");

let currentTheme=getTheme();

changeTheme();
function changeTheme(){
    const changeThemeButton=document.querySelector("#theme_change_button");
    changeThemeButton.addEventListener("click",(event)=>{
        const oldTheme=currentTheme;
        console.log("Theme change on clicking");
        if(currentTheme=="dark"){
            currentTheme="light";
        }
        else{
            currentTheme="dark";
        }

        setTheme(currentTheme);
        document.querySelector("html").classList.remove(oldTheme);
        document.querySelector("html").classList.add(currentTheme);
    });


}

function setTheme(theme){
    localStorage.setItem("theme",theme);
}

function getTheme(){
    let theme=localStorage.getItem("theme");
    return theme? theme:"light";
}