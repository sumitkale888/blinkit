# TODO - Category → Product Listing → Product Details (Blinkit-style)

- [x] Implement `CategoriesScreen` (fetch `/api/categories`, list categories, navigate to category products)
- [x] Implement `CategoryProductsScreen` (fetch `/api/products/category/{categoryId}`, list products with cards, skeleton/loading/empty/error)
- [x] Implement `ProductDetailsScreen` (fetch `/api/products/{id}`, show full details, skeleton/loading/empty/error)
- [x] Add reusable `ProductCard` component (used by listing screens)
- [x] Update React Navigation (`frontend/src/navigation/Navigation.tsx`) to register new screens + params
- [x] Update `ProductDashboard` to navigate:
  - category card -> `CategoryProductsScreen`
  - featured product -> `ProductDetailsScreen`
- [ ] Later: inspect backend cart endpoints and wire real “Add to Cart”
