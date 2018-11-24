const express = require("express");
const router = express.Router();
const mongoose = require("mongoose");
const multer = require("multer");
const jwt = require('jsonwebtoken');
const verificarAutenticacion = require('../middleware/verificar_autorizacion');

const Mensaje = require("../models/mensaje")

//metodo get que obtiene todos los mensajes
router.get("/", verificarAutenticacion, (req, res, next) => {
  Mensaje.find()
    .select("_id Emisor Mensaje Receptor Fecha Asunto Tabla NombreArchivo")
    .exec()
    .then(docs => {
      const response = {
        mensajes: docs.map(doc => {
          return {
            _id: doc._id,
            Emisor: doc.Emisor,
            Mensaje: doc.Mensaje,
            Receptor: doc.Receptor,
            Fecha: doc.Fecha,
            Asunto: doc.Asunto,
            Tabla: doc.Tabla,
            NombreArchivo: doc.NombreArchivo
          };
        })
      };
      const token = jwt.sign({
      },
       process.env.JWT_KEY,
        {
            //tiempo de expiración
            expiresIn: '1h'
       });
      //   if (docs.length >= 0) {
        res.status(200).json({
        docs, 
        token: token
      });
      /*   } else {
             res.status(404).json({
                 mensaje: 'Sin mensajes en el servidor'
             });
         }
      */
    })
    .catch(err => {
      console.log(err);
      res.status(500).json({
        error: err
      });
    });
});


//metodo post que ingresa mensajes
router.post("/", verificarAutenticacion, (req, res, next) => {
  const mensaje = new Mensaje({
    _id: new mongoose.Types.ObjectId(),
    Emisor: req.body.Emisor,
    Mensaje: req.body.Mensaje,
    Receptor: req.body.Receptor,
    Fecha: req.body.Fecha,
    Asunto: req.body.Asunto,
    Tabla: req.body.Tabla,
    NombreArchivo: req.body.NombreArchivo
  });
  mensaje
    .save()
    .then(result => {
      console.log(result);
      const token = jwt.sign({
      },
       process.env.JWT_KEY,
        {
            //tiempo de expiración
            expiresIn: '1h'
       });
      res.status(201).json({
          _id: result._id,
          Emisor: req.body.Emisor,
          Mensaje: req.body.Mensaje,
          Receptor: req.body.Receptor,
          Fecha: req.body.Fecha,
          Asunto: req.body.Asunto,
          Tabla: req.body.Tabla,
          NombreArchivo: req.body.NombreArchivo,
          token:token
      });
    })
    .catch(err => {
      console.log(err);
      res.status(500).json({
        error: err
      });
    });
});


//metodo get que obtiene un mensaje especifico al ingresar un ID del mensaje a buscar
router.get("/:mensajeId", verificarAutenticacion, (req, res, next) => {
  const id = req.params.mensajeId;
  Mensaje.findById(id)
    .select("_id Emisor Mensaje Receptor Fecha Asunto, Tabla, NombreArchivo")
    .exec()
    .then(doc => {
      console.log("Información del Servidor:", doc);
      if (doc) {
        const token = jwt.sign({
      },
       process.env.JWT_KEY,
        {
            //tiempo de expiración
            expiresIn: '1h'
       });
        res.status(200).json({
          doc,
          token: token    
        });
      } else {
        res
          .status(404)
          .json({ mensaje: "ID del mensaje invalido" });
      }
    })
    .catch(err => {
      console.log(err);
      res.status(500).json({ error: err });
    });
});

//metodo que elimina un mensaje al ingresar el ID del mensaje a eliminar
router.delete("/:mensajeId", verificarAutenticacion, (req, res, next) => {
  const id = req.params.mensajeId;
  Mensaje.remove({ _id: id })
    .exec()
    .then(result => {
      const token = jwt.sign({
      },
       process.env.JWT_KEY,
        {
            //tiempo de expiración
            expiresIn: '1h'
       });
      res.status(200).json({
          mensaje: 'Mensaje con id '+ id +' ha sido eliminado',
          token: token
      });
    })
    .catch(err => {
      console.log(err);
      res.status(500).json({
        error: err
      });
    });
});

module.exports = router;
