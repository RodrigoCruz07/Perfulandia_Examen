# Perfulandia_Examen
Este proyecto ha sido desarrollado con Java SDK 21 (versión 21.0.7) y está dividido en módulos independientes, cada uno con su propio servidor y base de datos H2.
Para probar este proyecto es necesario tener Postman
El localhost del modulo de perfume-inventario-purchase es de localhost:8080 con dominio de h2 jdbc:h2:file:./db/modulepurchase
el localhost del modulo de seller-user es de localhost:8081 con dominio de h2 jdbc:h2:file:./db/moduleuser
todos con el usuario sa y sin contraseña

Modulo user-seller
Este módulo gestiona el registro, modificación y autenticación de usuarios y vendedores del sistema. Incluye lógica para asignar roles, validar correos únicos y cifrar contraseñas para la autenticación de vendedores.

Funciones generales 
- Registrar usuarios con rol `USUARIO` por defecto
- Registrar vendedores con rol configurable (ej: `VENDEDOR`, `ADMIN`)
- Consultar, actualizar y eliminar usuarios
- Autenticación mediante Spring Security (`UserDetailsService`)
- Cifrado de contraseñas con `BCrypt`

Funciones de SellerService
-Registrar un nuevo vendedor
*ejemplo de post:
{
  "name": "Laura",
  "lastname": "González",
  "email": "laura@sellers.com",
  "password": "contrasenaSegura123",
  "phone": "912345678",
  "address": "Calle Falsa 123",
  "age": 30,
  "roleName": "VENDEDOR" // VENDEDOR,LOGISTICA,GERENTE 
}
Funciones de CustomUserDetailsService
-Implementación de UserDetailsService para autenticación de vendedores.
-El login se basa en el email del vendedor.
-Las contraseñas están cifradas con BCrypt.
-Se asigna el rol con prefijo ROLE_ para integración con Spring Security.

Funciones de UserService //El cual es ejecutado por el VENDEDOR previamente iniciado sesion y este obtener un token, sin ese token no puede hacer ninguna ejecucion de estas
//En Postman ocupar Authorization Bearer token y pegar el token , si no le aparece eso , en headers en Value poner Bearer (token) debe de haber espacio entre bearer y token para probar cualquier funcion de aca
-Registrar usuarios
*ejemplo de post:
{
  "name": "Juan",
  "lastname": "Pérez",
  "email": "juan@example.com",
  "phone": "912345678",
  "age": 28,
  "roleName": "USUARIO" //Existe 4 roles "VENDEDOR","USUARIO","LOGISTICA,"GERENTE"
}
-Obtener usuario por ID
-Actualizar usuario
-Eliminar usuario
-Listar todos los usuarios

Modulo Perfume-Inventory-Purchase
Este módulo permite gestionar el catálogo de perfumes, el stock disponible en inventario y el registro de compras realizadas por los usuarios. Se encarga de controlar el flujo de productos desde que se registran hasta que son vendidos, asegurando que haya suficiente stock antes de cada compra.

Funciones generales 
-Registrar, actualizar y eliminar perfumes.
-Registrar stock de perfumes en inventario.
-Consultar inventarios disponibles.
-Registrar compras asociadas a un usuario y un vendedor.
-Verificar stock antes de cada compra y descontarlo automáticamente.
-Listar y consultar todas las compras registradas.

Funciones de PerfumeService
-Registrar Perfume
*ejemplo:
{
  "name": "Eros",
  "brand": "Versace",
  "size": 100
}
-Obtener perfume: por ID.
-Actualizar perfume: nombre, marca y tamaño.
-Eliminar perfume: si existe.
-Listar perfumes: devuelve todos los perfumes registrados.

Funciones de InventoryService
-Registrar inventario
*ejemplo:
{
  "perfumeId": 1,
  "quantity": 10,
  "price": 12990,
  "location": "Bodega Central"
}
-Obtener inventario por ID.
-Eliminar inventario por ID.
-Listar inventarios.

Funciones de PurchaseService
-Registrar compra:
*Verifica existencia del perfume y stock suficiente.
*Descuenta cantidad del inventario.
*Registra compra con fecha y precio.
*ejemplo:
{
  "perfumeid": 1,
  "userid": 10,
  "sellerid": 5,
  "quantity": 2,
  "price": 25980
}
-Obtener compra por ID.
-Eliminar compra.
-Listar compras.


