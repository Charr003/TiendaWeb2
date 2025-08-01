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
import com.tienda.service.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
*/

import com.tienda.domain.Categoria;
import com.tienda.service.CategoriaService;
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


@Controller
@Slf4j // manejo de solicitud hhtp
@RequestMapping("/categoria")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/listado")
    public String inicio(Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias); // Se asocia lista categoria a la variable, toda la info se usa en la vista
        model.addAttribute("totalCategorias", categorias.size()); // Se asocia un dato a la variable para la vista
        return "/categoria/listado";
    }
    
    @GetMapping("/nuevo")
    public String categoriaNuevo(Categoria categoria) {
           return "/categoria/modifica";
       }

       @Autowired
       private FirebaseStorageServiceImpl firebaseStorageService;

       @PostMapping("/guardar")
       public String categoriaGuardar(Categoria categoria,
               @RequestParam("imagenFile") MultipartFile imagenFile) {        
           if (!imagenFile.isEmpty()) {
               categoriaService.save(categoria);
               categoria.setRutaImagen(
                       firebaseStorageService.cargaImagen(
                               imagenFile, 
                               "categoria", 
                               categoria.getIdCategoria()));
           }
           categoriaService.save(categoria);
           return "redirect:/categoria/listado";
       }

       @GetMapping("/eliminar/{idCategoria}")
       public String categoriaEliminar(Categoria categoria) {
           categoriaService.delete(categoria);
           return "redirect:/categoria/listado";
       }

       @GetMapping("/modificar/{idCategoria}")
       public String categoriaModificar(Categoria categoria, Model model) {
           categoria = categoriaService.getCategoria(categoria);
           model.addAttribute("categoria", categoria);
           return "/categoria/modifica";
       } 
    
}
