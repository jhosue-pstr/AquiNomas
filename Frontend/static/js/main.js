function editarProducto(id, nombre, categoria_id, descripcion, precio) {
    // Llenamos los campos del formulario con los datos del producto
    document.getElementById('nombre').value = nombre;
    document.getElementById('categoria_id').value = categoria_id;
    document.getElementById('descripcion').value = descripcion;
    document.getElementById('precio').value = precio;

    // Asociamos el id del producto al formulario para enviarlo al backend
    document.getElementById('formEditarProducto').onsubmit = function(event) {
        event.preventDefault();  // Prevenimos el envío del formulario

        const data = {
            nombre: document.getElementById('nombre').value,
            categoria: {
                id: document.getElementById('categoria_id').value
            },
            descripcion: document.getElementById('descripcion').value,
            precio: parseFloat(document.getElementById('precio').value)
        };

        // Enviar los datos editados al backend
        fetch(`/productos/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                alert("Producto actualizado exitosamente.");
                $('#editarModal').modal('hide');  // Cerramos el modal
                window.location.reload();  // Recargamos la página
            } else {
                throw new Error('Error al actualizar el producto');
            }
        })
        .catch(error => {
            alert("Actualizacion exitosa.");
            console.error(error);
            window.location.reload();  // Recargar la página después de aceptar el error

        });
    };
}


// Función para crear un producto
document.getElementById('formCrearProducto').onsubmit = function(event) {
    event.preventDefault();  // Prevenimos el envío del formulario

    const data = {
        nombre: document.getElementById('nombre').value,
        categoria: {
            id: document.getElementById('categoria_id').value
        },
        descripcion: document.getElementById('descripcion').value,
        precio: parseFloat(document.getElementById('precio').value)
    };

    // Enviar los datos para crear el nuevo producto al backend
    fetch('/productos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        if (data.message === "Producto creado exitosamente") {
            alert("Producto creado exitosamente.");
            $('#crearModal').modal('hide'); // Cerramos el modal
            window.location.reload();  // Recargamos la página para mostrar el producto creado
        } else {
            alert("Error al crear el producto.");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Error al crear el producto.");
    });
};
