/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.controller;

/**
 *
 * @author XPC
 */
/*
import com.tienda.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
*/

import com.tienda.domain.Producto;
import com.tienda.service.ProductoService;
import com.tienda.service.impl.FirebaseStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import com.tienda.domain.Categoria;
import com.tienda.service.CategoriaService;

@Controller
@Slf4j // manejo de solicitud http
@RequestMapping("/producto")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping("/listado")
    public String inicio(Model model) {
        var productos = productoService.getProductos(false);
        
        var categorias = categoriaService.getCategorias(false);         
        model.addAttribute("categorias", categorias);
        
        model.addAttribute("productos", productos); // Se asocia lista producto a la variable, toda la info se usa en la vista
        model.addAttribute("totalProductos", productos.size()); // Se asocia un dato a la variable para la vista
        return "/producto/listado";
    }
    
    @GetMapping("/nuevo")
    public String productoNuevo(Producto producto) {
           return "/producto/modifica";
       }

       @Autowired
       private FirebaseStorageServiceImpl firebaseStorageService;

       @PostMapping("/guardar")
       public String productoGuardar(Producto producto,
               @RequestParam("imagenFile") MultipartFile imagenFile) {        
           if (!imagenFile.isEmpty()) {
               productoService.save(producto);
               producto.setRutaImagen(
                       firebaseStorageService.cargaImagen(
                               imagenFile, 
                               "producto", 
                               producto.getIdProducto()));
           }
           productoService.save(producto);
           return "redirect:/producto/listado";
       }

       @GetMapping("/eliminar/{idProducto}")
       public String productoEliminar(Producto producto) {
           productoService.delete(producto);
           return "redirect:/producto/listado";
       }

       @GetMapping("/modificar/{idProducto}")
       public String productoModificar(Producto producto, Model model) {
           producto = productoService.getProducto(producto);
           var categorias = categoriaService.getCategorias(false);        
           model.addAttribute("categorias", categorias);
           model.addAttribute("producto", producto);
           return "/producto/modifica";
       } 
    
}
