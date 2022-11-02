VanillaTilt.init(document.querySelectorAll(".intro_content"), {
    max: 25,
    speed: 400,
    glare: true,
    "max-glare" : 1
});

VanillaTilt.init(document.querySelectorAll(".card_content_wrap"), {
    max: 25,
    speed: 400,
    glare: true,
    "max-glare" : 1
});

for(i = 1; i < 100; i++){
    j = 30;
    const para1 = document.createElement("li");
    const element = document.getElementById("bpwrap");
    para1.setAttribute('id', 'bp' + i);
    element.appendChild(para1);

    const para2 = document.createElement("div");
    para2.setAttribute('id', 'line' + i);
    para1.appendChild(para2);

    para2.setAttribute('class', 'bpf');

    document.getElementById("bp" + i).style.left = j*i - 40 + "px";
}

function myInterval(){
    for(i = 1; i < 100; i++){
        document.getElementById("line" + i).style.height = Math.floor(Math.random() * 100) + 1 + "%";
    }
}

setInterval("myInterval()", 1500);

const navlist = document.querySelectorAll('.lcn_list');
function activeLink(){
    navlist.forEach((item) =>
        item.classList.remove('active'));
    this.classList.add('active');
}
navlist.forEach((item) =>
    item.addEventListener('click', activeLink));

function toggleClass(){
    var cardWrap = document.getElementById('bpw');
    cardWrap.classList.toggle('toggleActive');
    var popup1 = document.getElementById('popup1');
    popup1.classList.toggle('toggleActive');
    var lcnav = document.getElementById('lc_nav_wrap');
    lcnav.classList.toggle('toggleActive');
}