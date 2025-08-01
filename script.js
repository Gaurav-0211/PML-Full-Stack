function generateCaptchaText() {
  const chars =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  return Array.from(
    { length: 6 },
    () => chars[Math.floor(Math.random() * chars.length)]
  ).join("");
}

function drawCaptcha(captcha, canvasId) {
  const canvas = document.getElementById(canvasId);
  const ctx = canvas.getContext("2d");
  const width = canvas.width;
  const height = canvas.height;

  ctx.clearRect(0, 0, width, height);

  const gradient = ctx.createLinearGradient(0, 0, width, height);
  gradient.addColorStop(0, "#f1c40f");
  gradient.addColorStop(1, "#e67e22");
  ctx.fillStyle = gradient;
  ctx.fillRect(0, 0, width, height);

  for (let i = 0; i < 5; i++) {
    ctx.strokeStyle = `rgba(${Math.random() * 255}, ${Math.random() * 255}, ${
      Math.random() * 255
    }, 0.5)`;
    ctx.beginPath();
    ctx.moveTo(Math.random() * width, Math.random() * height);
    ctx.lineTo(Math.random() * width, Math.random() * height);
    ctx.stroke();
  }

  const charSpacing = width / (captcha.length + 1);
  ctx.font = "20px Arial";
  ctx.fillStyle = "#2c3e50";
  ctx.textBaseline = "middle";

  for (let i = 0; i < captcha.length; i++) {
    const x = charSpacing * (i + 1);
    const y = height / 2;
    const angle = (Math.random() - 0.5) * 0.3;

    ctx.save();
    ctx.translate(x, y);
    ctx.rotate(angle);
    ctx.fillText(captcha[i], 0, 0);
    ctx.restore();
  }

  canvas.dataset.captcha = captcha;
}

function setupCaptcha() {
  drawCaptcha(generateCaptchaText(), "login-captcha-canvas");
  drawCaptcha(generateCaptchaText(), "register-captcha-canvas");
}

document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("login-form");
  const registerForm = document.getElementById("register-form");
  const showLogin = document.getElementById("show-login");
  const showRegister = document.getElementById("show-register");

  showLogin.addEventListener("click", () => {
    loginForm.classList.replace("hidden", "visible");
    registerForm.classList.replace("visible", "hidden");
    setupCaptcha();
  });

  showRegister.addEventListener("click", () => {
    registerForm.classList.replace("hidden", "visible");
    loginForm.classList.replace("visible", "hidden");
    setupCaptcha();
  });

  setupCaptcha(); // Default view

  // Login Form Submission
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("login-email").value.trim();
    const password = document.getElementById("login-password").value;
    const captchaInput = document
      .getElementById("login-captcha-input")
      .value.trim();
    const captcha = document.getElementById("login-captcha-canvas").dataset
      .captcha;

    if (captchaInput !== captcha) {
      alert("Incorrect CAPTCHA.");
      setupCaptcha();
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const message = await response.text();
      if (response.ok) {
        alert(message);
        window.location.href = "dashboard.html";
      } else {
        alert("Login failed: " + message);
        setupCaptcha();
      }
    } catch (err) {
      console.error(err);
      alert("Error during login.");
    }
  });

  // Registration Form Submission
  registerForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const name = document.getElementById("register-name").value.trim();
    const email = document.getElementById("register-email").value.trim();
    const password = document.getElementById("register-password").value;

    const confirmPassword = document.getElementById(
      "register-confirm-password"
    ).value;
    const captchaInput = document
      .getElementById("register-captcha-input")
      .value.trim();
    const captcha = document.getElementById("register-captcha-canvas").dataset
      .captcha;

    if (password !== confirmPassword) {
      alert("Passwords do not match.");
      return;
    }

    if (captchaInput !== captcha) {
      alert("Incorrect CAPTCHA.");
      setupCaptcha();
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password }),
      });

      const result = await response.json();
      if (response.ok) {
        alert("Registration successful!");
        showLogin.click();
      } else {
        alert("Registration failed.");
        setupCaptcha();
      }
    } catch (err) {
      console.error(err);
      alert("Error during registration.");
    }
  });
});
