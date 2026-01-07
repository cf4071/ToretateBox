document.addEventListener("DOMContentLoaded", () => {
    const btn = document.querySelector(".hamburger-btn");
    const menu = document.querySelector(".hamburger-menu");

    if (btn && menu) {
        btn.addEventListener("click", () => {
            menu.classList.toggle("hidden");
        });
    }
});
