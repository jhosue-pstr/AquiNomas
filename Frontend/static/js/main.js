(function () {
  "use strict";

  // Función para manejar la selección de idioma
  function handleLanguageSelection(language) {
    // Ocultar todos los checkmarks
    document
      .querySelectorAll(".bi-check")
      .forEach((check) => (check.style.visibility = "hidden"));

    // Mostrar el checkmark del idioma seleccionado
    if (language === "Español") {
      document.getElementById("checkSpanish").style.visibility = "visible";
    } else if (language === "English") {
      document.getElementById("checkEnglish").style.visibility = "visible";
    }
    // Actualizar el texto del botón
    document.getElementById(
      "dropdownLangCurrency"
    ).innerHTML = `${language} / ${
      document.getElementById("dropdownLangCurrency").innerHTML.split(" / ")[1]
    }`;
  }

  // Función para manejar la selección de moneda
  function handleCurrencySelection(currency) {
    // Ocultar todos los checkmarks de moneda
    document
      .querySelectorAll(".bi-check")
      .forEach((check) => (check.style.visibility = "hidden"));

    // Mostrar el checkmark de la moneda seleccionada
    if (currency === "PEN") {
      document.getElementById("checkPEN").style.visibility = "visible";
    } else if (currency === "USD") {
      document.getElementById("checkPEN").style.visibility = "hidden";
    }
    // Actualizar el texto del botón
    document.getElementById("dropdownLangCurrency").innerHTML = `${
      document.getElementById("dropdownLangCurrency").innerHTML.split(" / ")[0]
    } / ${currency}`;
  }

  // Agregar eventos para el idioma
  document
    .getElementById("selectEnglish")
    .addEventListener("click", function () {
      handleLanguageSelection("English");
    });
  document
    .getElementById("selectSpanish")
    .addEventListener("click", function () {
      handleLanguageSelection("Español");
    });

  // Agregar eventos para la moneda
  document.getElementById("selectPEN").addEventListener("click", function () {
    handleCurrencySelection("PEN");
  });
  document.getElementById("selectUSD").addEventListener("click", function () {
    handleCurrencySelection("USD");
  });

  /**
   * FAQ Pop-up
   */
  document.querySelectorAll('.faq-item h3').forEach((faqItem) => {
    faqItem.addEventListener('click', () => {
      // Obtener el texto de la pregunta
      const questionText = faqItem.innerText;

      // Variables para la respuesta y la imagen
      let answerText = "";
      let imgUrl = "";

      // Aquí agregamos las respuestas personalizadas y las imágenes
      switch (questionText) {
        case "¿Qué tipo de maquinaria textil importan y ofrecen?":
          answerText = "Ofrecemos maquinaria textil para la fabricación de prendas de vestir, maquinaria de estampado, bordado y confección.";
          imgUrl = "path_to_image_1.jpg"; // Cambia con el enlace de tu imagen
          break;
        case "¿Cómo apoyan la revalorización de las artesanías locales?":
          answerText = "Apoyamos el diseño y comercialización de artesanías locales, combinando tradición con tecnología para mercados internacionales.";
          imgUrl = "path_to_image_2.jpg"; // Cambia con el enlace de tu imagen
          break;
        case "¿Cuáles son los principales mercados internacionales a los que exportan?":
          answerText = "Exportamos principalmente a América del Norte y Europa, con énfasis en los mercados que buscan productos de calidad y diseño único.";
          imgUrl = "path_to_image_3.jpg"; // Cambia con el enlace de tu imagen
          break;
        case "¿Ofrecen asesoría técnica para la implementación de maquinaria?":
          answerText = "Sí, ofrecemos asesoría completa, desde la instalación hasta el mantenimiento de la maquinaria que proveemos.";
          imgUrl = "path_to_image_4.jpg"; // Cambia con el enlace de tu imagen
          break;
        case "¿Qué certificaciones o garantías tienen los productos y maquinaria que comercializan?":
          answerText = "Nuestros productos cuentan con certificaciones internacionales y ofrecemos garantía de fábrica para todas nuestras maquinarias.";
          imgUrl = "path_to_image_5.jpg"; // Cambia con el enlace de tu imagen
          break;
        case "¿Cómo puedo iniciar un proceso de exportación con Infotel Business?":
          answerText = "Puedes contactar a nuestro equipo de ventas para que te asesoren en el proceso de exportación, desde la selección del producto hasta la entrega.";
          imgUrl = "path_to_image_6.jpg"; // Cambia con el enlace de tu imagen
          break;
      }

      // Mostrar el popup con la pregunta y la respuesta
      document.getElementById('faq-popup-question').innerText = questionText;
      document.getElementById('faq-popup-answer').innerText = answerText;
      document.getElementById('faq-popup-img').src = imgUrl;
      document.getElementById('faq-popup').style.display = 'block';  // Mostrar el popup
    });
  });

  // Función para cerrar el popup
  document.getElementById('close-popup-btn').addEventListener('click', function() {
    document.getElementById('faq-popup').style.display = 'none';  // Ocultar el popup
  });

  /**
   * Apply .scrolled class to the body as the page is scrolled down
   */
  function toggleScrolled() {
    const selectBody = document.querySelector("body");
    const selectHeader = document.querySelector("#header");
    if (
      !selectHeader.classList.contains("scroll-up-sticky") &&
      !selectHeader.classList.contains("sticky-top") &&
      !selectHeader.classList.contains("fixed-top")
    )
      return;
    window.scrollY > 100
      ? selectBody.classList.add("scrolled")
      : selectBody.classList.remove("scrolled");
  }

  document.addEventListener("scroll", toggleScrolled);
  window.addEventListener("load", toggleScrolled);

  /**
   * Mobile nav toggle
   */
  const mobileNavToggleBtn = document.querySelector(".mobile-nav-toggle");

  function mobileNavToogle() {
    document.querySelector("body").classList.toggle("mobile-nav-active");
    mobileNavToggleBtn.classList.toggle("bi-list");
    mobileNavToggleBtn.classList.toggle("bi-x");
  }
  mobileNavToggleBtn.addEventListener("click", mobileNavToogle);

  /**
   * Hide mobile nav on same-page/hash links
   */
  document.querySelectorAll("#navmenu a").forEach((navmenu) => {
    navmenu.addEventListener("click", () => {
      if (document.querySelector(".mobile-nav-active")) {
        mobileNavToogle();
      }
    });
  });

  /**
   * Toggle mobile nav dropdowns
   */
  document.querySelectorAll(".navmenu .toggle-dropdown").forEach((navmenu) => {
    navmenu.addEventListener("click", function (e) {
      e.preventDefault();
      this.parentNode.classList.toggle("active");
      this.parentNode.nextElementSibling.classList.toggle("dropdown-active");
      e.stopImmediatePropagation();
    });
  });

  /**
   * Preloader
   */
  const preloader = document.querySelector("#preloader");
  if (preloader) {
    window.addEventListener("load", () => {
      preloader.remove();
    });
  }

  /**
   * Scroll top button
   */
  let scrollTop = document.querySelector(".scroll-top");

  function toggleScrollTop() {
    if (scrollTop) {
      window.scrollY > 100
        ? scrollTop.classList.add("active")
        : scrollTop.classList.remove("active");
    }
  }
  scrollTop.addEventListener("click", (e) => {
    e.preventDefault();
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  });

  window.addEventListener("load", toggleScrollTop);
  document.addEventListener("scroll", toggleScrollTop);

  /**
   * Animation on scroll function and init
   */
  function aosInit() {
    AOS.init({
      duration: 600,
      easing: "ease-in-out",
      once: true,
      mirror: false,
    });
  }
  window.addEventListener("load", aosInit);

  /**
   * Auto generate the carousel indicators
   */
  document
    .querySelectorAll(".carousel-indicators")
    .forEach((carouselIndicator) => {
      carouselIndicator
        .closest(".carousel")
        .querySelectorAll(".carousel-item")
        .forEach((carouselItem, index) => {
          if (index === 0) {
            carouselIndicator.innerHTML += `<li data-bs-target="#${
              carouselIndicator.closest(".carousel").id
            }" data-bs-slide-to="${index}" class="active"></li>`;
          } else {
            carouselIndicator.innerHTML += `<li data-bs-target="#${
              carouselIndicator.closest(".carousel").id
            }" data-bs-slide-to="${index}"></li>`;
          }
        });
    });

  /**
   * Initiate glightbox
   */
  const glightbox = GLightbox({
    selector: ".glightbox",
  });

  /**
   * Initiate Pure Counter
   */
  new PureCounter();

  /**
   * Init swiper sliders
   */
  function initSwiper() {
    document.querySelectorAll(".init-swiper").forEach(function (swiperElement) {
      let config = JSON.parse(
        swiperElement.querySelector(".swiper-config").innerHTML.trim()
      );

      if (swiperElement.classList.contains("swiper-tab")) {
        initSwiperWithCustomPagination(swiperElement, config);
      } else {
        new Swiper(swiperElement, config);
      }
    });
  }

  window.addEventListener("load", initSwiper);

  /**
   * Frequently Asked Questions Toggle
   */
  document
    .querySelectorAll(".faq-item h3, .faq-item .faq-toggle")
    .forEach((faqItem) => {
      faqItem.addEventListener("click", () => {
        faqItem.parentNode.classList.toggle("faq-active");
      });
    });

  /**
   * Correct scrolling position upon page load for URLs containing hash links.
   */
  window.addEventListener("load", function (e) {
    if (window.location.hash) {
      if (document.querySelector(window.location.hash)) {
        setTimeout(() => {
          let section = document.querySelector(window.location.hash);
          let scrollMarginTop = getComputedStyle(section).scrollMarginTop;
          window.scrollTo({
            top: section.offsetTop - parseInt(scrollMarginTop),
            behavior: "smooth",
          });
        }, 100);
      }
    }
  });

  /**
   * Navmenu Scrollspy
   */
  let navmenulinks = document.querySelectorAll(".navmenu a");

  function navmenuScrollspy() {
    navmenulinks.forEach((navmenulink) => {
      if (!navmenulink.hash) return;
      let section = document.querySelector(navmenulink.hash);
      if (!section) return;
      let position = window.scrollY + 200;
      if (
        position >= section.offsetTop &&
        position <= section.offsetTop + section.offsetHeight
      ) {
        document
          .querySelectorAll(".navmenu a.active")
          .forEach((link) => link.classList.remove("active"));
        navmenulink.classList.add("active");
      } else {
        navmenulink.classList.remove("active");
      }
    });
  }
  window.addEventListener("load", navmenuScrollspy);
  document.addEventListener("scroll", navmenuScrollspy);




  ///////////////////////////////////


































  // Constantes del chatbot
  // Constantes del chatbot
  // Constantes del chatbot
const input = document.getElementById("chat-input");
const sendBtn = document.getElementById("chat-send");
const messages = document.getElementById("chat-messages");

// Estado actual del bot
let estadoActual = null; // "promocion", "redes", "soporte", "faq", etc.
let esperandoSeleccionInicial = false; // Para controlar si mostramos el menú principal

// Función para agregar mensajes
function addMessage(text, sender) {
  const div = document.createElement("div");
  div.classList.add("message", sender);

  const avatar = document.createElement("img");
  avatar.classList.add("avatar");
  avatar.src =
    sender === "user" ? "static/img/usuario.png" : "static/img/botxd.jpg";
  avatar.alt = sender === "user" ? "Usuario" : "Chatbot";

  const messageText = document.createElement("div");
  messageText.classList.add("text");
  messageText.innerHTML = text;

  div.appendChild(avatar);
  div.appendChild(messageText);

  messages.appendChild(div);
  messages.scrollTop = messages.scrollHeight;
}

// Función de bienvenida
function mensajeBienvenida() {
  esperandoSeleccionInicial = true;
  addMessage(
    `¡Hola! Bienvenido a Infotel Business Perú. Por favor, selecciona una opción para comenzar:<br>
    1. Promociones y descuentos<br>
    2. Redes sociales y contacto<br>
    3. Preguntas frecuentes<br>
    4. Soporte técnico básico`,
    "bot"
  );
}

// Filtro de agradecimientos
function contienePalabraGracias(texto) {
  const palabrasGracias = ["gracias", "muchas gracias", "te agradezco", "mil gracias"];
  return palabrasGracias.some((palabra) => texto.includes(palabra));
}

// Detectores de palabras clave
function contienePalabraPromocion(texto) {
  const palabrasPromo = ["promoción", "descuento", "oferta", "cupon", "rebaja"];
  return palabrasPromo.some((palabra) => texto.includes(palabra));
}

function contienePalabraRedes(texto) {
  const palabrasRedes = [
    "redes",
    "facebook",
    "instagram",
    "whatsapp",
    "contacto",
    "twitter",
    "ubicación",
    "dirección",
  ];
  return palabrasRedes.some((palabra) => texto.includes(palabra));
}

function contienePalabraSoporte(texto) {
  const palabrasSoporte = [
    "soporte",
    "ayuda",
    "duda",
    "problema",
    "consulta",
    "información",
    "contactar",
  ];
  return palabrasSoporte.some((palabra) => texto.includes(palabra));
}

function contienePalabraFAQ(texto) {
  const palabrasFAQ = [
    "importar",
    "exportar",
    "entrega",
    "tiempo",
    "costo",
    "precio",
    "envio",
    "pregunta frecuente",
    "faq",
    "preguntas frecuentes",
  ];
  return palabrasFAQ.some((palabra) => texto.includes(palabra));
}

// Función para manejar la respuesta del usuario
async function getResponse(message) {
  const lower = message.toLowerCase().trim();

  // Si el usuario agradece
  if (contienePalabraGracias(lower)) {
    return "¡Gracias a ti por contactarnos! 😊 ¿En qué más puedo ayudarte?";
  }

  // Si está pidiendo las opciones iniciales nuevamente
  if (
    lower.includes("opciones") || 
    lower.includes("tus opciones") || 
    lower.includes("mostrar opciones") || 
    lower.includes("ver opciones") || 
    lower.includes("qué hacer") || 
    lower.includes("qué puedo hacer") || 
    lower.includes("ver opciones disponibles") || 
    lower.includes("mostrar menú") || 
    lower.includes("quiero opciones") || 
    lower.includes("menu") || 
    lower.includes("mis opciones")
  ) {
    esperandoSeleccionInicial = true; // Restablecemos la espera para las opciones
    return `¡Hola! Bienvenido a Infotel Business Perú. Por favor, selecciona una opción para comenzar:<br>
    1. Promociones y descuentos<br>
    2. Redes sociales y contacto<br>
    3. Preguntas frecuentes<br>
    4. Soporte técnico básico`;
  }

  // Si está esperando la selección inicial
  if (esperandoSeleccionInicial) {
    esperandoSeleccionInicial = false; // Ya respondimos a la bienvenida

    if (lower === "1") {
      estadoActual = "promocion";
      return `Actualmente tenemos las siguientes promociones vigentes:<br>
      1. 15% de descuento en maquinaria textil seleccionada.<br>
      2. Envío gratuito para pedidos mayores a S/ 500.<br>
      3. Cupones especiales para clientes recurrentes.<br>
      Por favor responde con el número de la opción que te interesa.`;
    } else if (lower === "2") {
      estadoActual = "redes";
      return `Puedes seguirnos y contactarnos en nuestras redes sociales:<br>
      1. Facebook<br>
      2. Instagram<br>
      3. WhatsApp<br>
      4. Dirección de la empresa<br>
      Por favor responde con el número de la opción que te interesa.`;
    } else if (lower === "3") {
      estadoActual = "faq";
      return `Preguntas frecuentes:<br>
      1. ¿Cómo puedo importar o exportar?<br>
      2. ¿Cuánto tardan los envíos?<br>
      3. ¿Cuáles son los costos asociados?<br>
      Por favor responde con el número de la opción que te interesa.`;
    } else if (lower === "4") {
      estadoActual = "soporte";
      return `Soporte técnico básico:<br>
      1. Información sobre productos y servicios.<br>
      2. Estado de tus pedidos.<br>
      3. Políticas de devolución.<br>
      4. Horarios de atención.<br>
      Por favor responde con el número de la opción que te interesa.`;
    } else {
      esperandoSeleccionInicial = true; // No es opción válida, volvemos a preguntar
      return "Por favor selecciona una opción válida: 1, 2, 3 o 4.";
    }
  }

  // Aquí los estados para las subopciones como antes
  if (estadoActual === "promocion") {
    estadoActual = null;
    if (lower === "1") {
      return "Has seleccionado: 15% de descuento en maquinaria textil seleccionada.";
    } else if (lower === "2") {
      return "Has seleccionado: Envío gratuito para pedidos mayores a S/ 500.";
    } else if (lower === "3") {
      return "Has seleccionado: Cupones especiales para clientes recurrentes.";
    } else {
      estadoActual = "promocion";
      return "Por favor selecciona una opción válida: 1, 2 o 3.";
    }
  }

  if (estadoActual === "redes") {
    estadoActual = null;
    if (lower === "1") {
      return `Síguenos en Facebook: <a href="https://www.facebook.com/infotelperu" target="_blank">Infotel Perú</a>`;
    } else if (lower === "2") {
      return `Síguenos en Instagram: <a href="https://www.instagram.com/infotelperu" target="_blank">infotelperu</a>`;
    } else if (lower === "3") {
      return `Contáctanos por WhatsApp: <a href="https://wa.me/51981141413" target="_blank">+51 981141413</a>`;
    } else if (lower === "4") {
      return `Nuestra dirección es:<br>Pasaje Ayaviri Mz. Ñ Lt 18F, Urb. San Francisco, Juliaca, Perú.`;
    } else {
      estadoActual = "redes";
      return "Por favor selecciona una opción válida: 1, 2, 3 o 4.";
    }
  }

  if (estadoActual === "faq") {
    estadoActual = null;
    if (lower === "1") {
      return "Puedes importar productos contactando a nuestro equipo de ventas, te ayudamos en todo el proceso.";
    } else if (lower === "2") {
      return "Los envíos suelen tardar entre 5 y 10 días hábiles, dependiendo del destino.";
    } else if (lower === "3") {
      return "Los costos asociados varían según el tipo de producto y destino, consulta con nosotros para detalles específicos.";
    } else {
      estadoActual = "faq";
      return "Por favor selecciona una opción válida: 1, 2 o 3.";
    }
  }

  if (estadoActual === "soporte") {
    estadoActual = null;
    if (lower === "1") {
      return "Información sobre productos y servicios: Maquinaria textil, repuestos, accesorios, artesanías, prendas, etc.";
    } else if (lower === "2") {
      return "Estado de tus pedidos: Puedes consultar el estado y fechas estimadas.";
    } else if (lower === "3") {
      return "Políticas de devolución: Detalles sobre cómo hacer una devolución o cambio.";
    } else if (lower === "4") {
      return "Horarios de atención: Nuestro horario es de lunes a viernes, de 9 AM a 6 PM.";
    } else {
      estadoActual = "soporte";
      return "Por favor selecciona una opción válida del 1 al 4.";
    }
  }

  // Respuestas por palabras clave sin estados
  if (contienePalabraPromocion(lower)) {
    estadoActual = "promocion";
    return `Actualmente tenemos las siguientes promociones vigentes:<br>
      1. 15% de descuento en maquinaria textil seleccionada.<br>
      2. Envío gratuito para pedidos mayores a S/ 500.<br>
      3. Cupones especiales para clientes recurrentes.<br>
      Por favor responde con el número de la opción que te interesa.`;
  }

  if (contienePalabraRedes(lower)) {
    estadoActual = "redes";
    return `Puedes seguirnos y contactarnos en nuestras redes sociales:<br>
      1. Facebook<br>
      2. Instagram<br>
      3. WhatsApp<br>
      4. Dirección de la empresa<br>
      Por favor responde con el número de la opción que te interesa.`;
  }

  if (contienePalabraSoporte(lower)) {
    estadoActual = "soporte";
    return `¡Hola! Soy tu asistente virtual para ayudarte con tus consultas.<br>
      Puedes preguntarme sobre:<br>
      1. Información sobre nuestros productos y servicios.<br>
      2. Estado de tus pedidos.<br>
      3. Políticas de devolución.<br>
      4. Horarios de atención.<br>
      Por favor responde con el número de la opción que te interesa.`;
  }

  if (contienePalabraFAQ(lower)) {
    estadoActual = "faq";
    return `Preguntas frecuentes:<br>
      1. ¿Cómo puedo importar o exportar?<br>
      2. ¿Cuánto tardan los envíos?<br>
      3. ¿Cuáles son los costos asociados?<br>
      Por favor responde con el número de la opción que te interesa.`;
  }

  // Si no es ninguna opción, llamar IA
  try {
    const response = await fetch("https://openrouter.ai/api/v1/chat/completions", {
      method: "POST",
      headers: {
        Authorization:
          "Bearer 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",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        model: "deepseek/deepseek-r1-0528-qwen3-8b:free",
        messages: [{ role: "user", content: message }],
      }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data.choices[0].message.content;
  } catch (error) {
    console.error(error);
    return "Lo siento, no puedo responder ahora. Por favor intenta más tarde.";
  }
}

// Evento para enviar mensaje al hacer click
sendBtn.addEventListener("click", async () => {
  const text = input.value.trim();
  if (!text) return;
  addMessage(text, "user");
  input.value = "";

  const response = await getResponse(text);
  addMessage(response, "bot");
});

// Permitir enviar mensaje con tecla Enter
input.addEventListener("keydown", (e) => {
  if (e.key === "Enter") sendBtn.click();
});

// Cuando se abra el chat, envía mensaje bienvenida
document.getElementById("chatbot-btn").addEventListener("click", () => {
  // Solo mostrar bienvenida si no hay mensajes (evitar múltiples mensajes)
  if (messages.children.length === 0) {
    mensajeBienvenida();
  }
});






})();
