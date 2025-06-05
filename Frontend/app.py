from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy
from html import escape

app = Flask(__name__)

# Configuración de la base de datos
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:@localhost/producto_db'
db = SQLAlchemy(app)
# Registrar el filtro 'escape' de Python
app.jinja_env.filters['escape'] = escape
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
    # Obtener las categorías de la base de datos
    categorias = Categoria.query.all()
    page = request.args.get('page', 1, type=int)
    productos = Producto.query.paginate(page=page, per_page=10)

    # Pasamos tanto los productos como las categorías a la plantilla
    return render_template('index.html', productos=productos, categorias=categorias)


# Ruta para editar un producto
@app.route('/productos/<int:id>', methods=['PUT'])
def editar_producto(id):
    producto = Producto.query.get_or_404(id)  # Obtiene el producto por su ID

    # Obtener los datos del cuerpo de la solicitud (JSON)
    data = request.get_json()

    if data:
        try:
            # Actualizamos los valores del producto
            producto.nombre = data.get('nombre', producto.nombre)
            producto.categoria_id = data['categoria']['id']
            producto.descripcion = data.get('descripcion', producto.descripcion)
            producto.precio = data.get('precio', producto.precio)

            # Guardamos los cambios en la base de datos
            db.session.commit()

            # Si no hay errores, retornamos una respuesta exitosa
            return jsonify({"message": "Producto actualizado exitosamente"}), 200
        except Exception as e:
            db.session.rollback()
            # Log de la excepción
            print(f"Error al actualizar el producto: {e}")
            return jsonify({"message": "Error al actualizar el producto", "error": str(e)}), 400
    else:
        return jsonify({"message": "Datos incompletos"}), 400


@app.route('/eliminar/<int:id>', methods=['GET', 'POST'])
def eliminar_producto(id):
    producto = Producto.query.get_or_404(id)  # Obtiene el producto por su ID

    # Eliminar el producto de la base de datos
    db.session.delete(producto)
    db.session.commit()

    # Redirigir a la página principal después de eliminar
    return redirect(url_for('listar_productos'))










    # Ruta para crear un producto
@app.route('/productos', methods=['POST'])
def crear_producto():
    # Obtener los datos del cuerpo de la solicitud (JSON)
    data = request.get_json()

    # Creamos un nuevo producto con los datos recibidos
    nuevo_producto = Producto(
        nombre=data['nombre'],
        categoria_id=data['categoria']['id'],
        descripcion=data['descripcion'],
        precio=data['precio']
    )

    try:
        db.session.add(nuevo_producto)
        db.session.commit()
        return jsonify({"message": "Producto creado exitosamente"}), 201
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "Error al crear el producto", "error": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True)


