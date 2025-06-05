import pymysql
pymysql.install_as_MySQLdb()  # Esto hace que SQLAlchemy use PyMySQL en lugar de MySQLdb.

from flask import Flask, render_template
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)

# Cambiar la URI para usar PyMySQL como conector
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:@localhost/producto_db'

# Instanciamos SQLAlchemy
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
    # Consultamos todos los productos con la categoría asociada
    productos = Producto.query.all()

    # Pasamos los productos a la plantilla HTML
    return render_template('index.html', productos=productos)

if __name__ == '__main__':
    app.run(debug=True)
