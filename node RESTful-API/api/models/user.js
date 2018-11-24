//se requiere mongoose
const mongoose = require('mongoose');

//schema constructor
const usersSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    Username: { type: String,required: true,unique: true},
    Contraseña: { type: String, required: true },
    Nombre: { type: String, required: true },
    Correo: { type: String,required: true ,unique: true,
        match: /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
    },
});

//modelo de compilar de schema
//mongoose creara una coleccion en la base de datos para el modelo "Users" usando el usersSchema
module.exports = mongoose.model('Users', usersSchema);