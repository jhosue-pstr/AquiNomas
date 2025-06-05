from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)

# Configuración de la base de datos
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:@localhost/producto_db'
db = SQLAlchemy(app)

# Modelo de Producto
class Producto(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(255))
    categoria_id = db.Column(db.Integer, db.ForeignKey('categoria.id'))
    descripcion = db.Column(db.Text)
    precio = db.Column(db.Float)

    categoria = db.relationship('Categoria', backref='productos')

# Modelo de Categoria
class Categoria(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(255))

@app.route('/')
def listar_productos():
    # Paginación: Obtener los primeros 10 productos
    page = request.args.get('page', 1, type=int)
    productos = Producto.query.paginate(page=page, per_page=10)

    # Pasamos los productos a la plantilla HTML
    return render_template('index.html', productos=productos)

# Ruta para editar un producto
@app.route('/productos/<int:id>', methods=['PUT'])
def editar_producto(id):
    producto = Producto.query.get_or_404(id)  # Obtiene el producto por su ID

    # Obtener los datos del cuerpo de la solicitud (JSON)
    data = request.get_json()

    if data:
        producto.nombre = data.get('nombre', producto.nombre)
        producto.categoria_id = data['categoria']['id']  # Tomamos el ID de la categoría desde el JSON
        producto.descripcion = data.get('descripcion', producto.descripcion)
        producto.precio = data.get('precio', producto.precio)

        # Guardar cambios en la base de datos
        db.session.commit()

        # Retornar una respuesta exitosa
        return jsonify({"message": "Producto actualizado exitosamente"}), 200

    return jsonify({"message": "No se pudo actualizar el producto"}), 400


@app.route('/eliminar/<int:id>', methods=['GET', 'POST'])
def eliminar_producto(id):
    producto = Producto.query.get_or_404(id)  # Obtiene el producto por su ID

    # Eliminar el producto de la base de datos
    db.session.delete(producto)
    db.session.commit()

    # Redirigir a la página principal después de eliminar
    return redirect(url_for('listar_productos'))

if __name__ == '__main__':
    app.run(debug=True)