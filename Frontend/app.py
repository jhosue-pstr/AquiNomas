from flask import Flask, render_template, request  # Asegúrate de importar 'request'

import pymysql
pymysql.install_as_MySQLdb()  # Esto hace que SQLAlchemy use PyMySQL en lugar de MySQLdb.

from flask import Flask, render_template, request  # Importa 'request'
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
    page = request.args.get('page', 1, type=int)  # Si no se pasa un valor, la página predeterminada es 1
    productos = Producto.query.paginate(page=page, per_page=10)  # 10 productos por página

    # Pasamos los productos a la plantilla HTML
    return render_template('index.html', productos=productos)


if __name__ == '__main__':
    app.run(debug=True)
