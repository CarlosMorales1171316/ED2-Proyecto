const mongoose = require('mongoose');

const mensajesSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    Emisor: { type: String, required: true },
    Mensaje: { type: String, required: true },
    Receptor: { type: String, required: true },
    Fecha: { type: String, required: true },
    Asunto: { type: String, required: true },
    Tabla: { type: String, required: true },
    NombreArchivo: { type: String, required: true }
    
});

module.exports = mongoose.model('Mensajes', mensajesSchema);