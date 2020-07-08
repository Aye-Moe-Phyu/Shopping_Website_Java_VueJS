
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Index.vue') },
      { path: '/signin', component: () => import('pages/SignIn') },
      { path: '/upload_new_product', component: () => import('pages/upload_new_product') },
      { path: '/view_upload_new_product', component: () => import('pages/view_upload_new_product') },
      { path: '/user_account_info', component: () => import('pages/user_account_info') },
      { path: '/product_detail', component: () => import('pages/product_detail') },
      { path: '/Shopping_Card_List', component: () => import('pages/Shopping_Card_List') },
      { path: '/Check_Out', component: () => import('pages/Check_Out') }
    ]
  }
]

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('pages/Error404.vue')
  })
}

export default routes
