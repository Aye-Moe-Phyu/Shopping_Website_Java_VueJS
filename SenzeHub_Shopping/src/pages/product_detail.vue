<template>
  <q-layout view="hHh LpR fFf">
    <q-header elevated class="bg-primary text-white">
      <div class="q-pa-md">
    <div class="row no-wrap">
      <q-toolbar class="col-4">
        <q-btn dense flat round icon="menu" @click="left = !left"/>
        <q-toolbar-title>
          <q-avatar icon="alarm_add" size="50px"/>
          Senze Hub
        </q-toolbar-title>
      </q-toolbar>
      <q-toolbar class="col-5 bg-primary text-white">
      <q-input dark standout v-model="text" input-class="text-left" class="q-ml-xl" maxlength="12" style="margin-bottom:0px;width:475px" @keyup.enter="Search_Product(text)">
        <template v-slot:append>
          <q-icon name="close" class="cursor-pointer" />
          <q-icon name="search" />
        </template>
      </q-input>
      </q-toolbar>

      <q-toolbar class="col-3 bg-primary text-white">
      <q-btn round color="primary" icon="shopping_cart" @click="Show_Shopping_Cart()">
        <q-badge color="red" floating transparent>
        {{quantity_unique_product}}
      </q-badge>
      </q-btn>
      </q-toolbar>

      </div>
      </div>
    </q-header>
    <q-drawer show-if-above v-model="left" side="left" bordered>
    <!-- drawer content -->
    <div class="q-pa-md" >
    <q-list bordered style="width:280px;" id="q_list_id" link to="/">
      <q-item clickable v-ripple link to="/" >
        <q-item-section avatar>
          <q-avatar color="blue" text-color="white" icon="home" />
        </q-item-section>
        <q-item-section>Home</q-item-section>
      </q-item>

      <q-item clickable v-ripple>
        <q-item-section avatar>
          <q-avatar color="blue" text-color="white" icon="logout" />
        </q-item-section>
        <q-item-section>Discount Items</q-item-section>
      </q-item>

      <q-separator />

      <q-item clickable v-ripple  link to="/signin" id='user_ele_id'>
        <q-item-section avatar>
          <q-avatar color="blue" icon="account_box" id="login_user_icon_id">
          </q-avatar>
        </q-item-section>
        <q-item-section>
        <span id="login_user_name_label_id" style="color:black;"> Login </span>
        <span id="login_account_name_label_id" style="color:black;"></span>
        </q-item-section>
      </q-item>

        <q-item clickable v-ripple v-if="this.$session.has('account_name')" link to="/upload_new_product">
        <q-item-section avatar>
          <q-avatar color="blue" text-color="white" icon="backup" />
        </q-item-section>
        <q-item-section>Upload_New_Product</q-item-section>
      </q-item>

      <q-item clickable v-ripple v-if="this.$session.has('account_name')" @click="LogOut">
        <q-item-section avatar>
          <q-avatar color="blue" text-color="white" icon="logout" />
        </q-item-section>
        <q-item-section>Log Out</q-item-section>
      </q-item>

    </q-list>
    </div>
    </q-drawer>
    <q-page-container>
<q-page class="q-pa-md">

<div class="q-pa-md">
  <!-- Columns start at 50% wide on mobile and bump up to 33.3% wide on desktop -->
  <div class="row">

      <div class="col-12 col-md-8">
      <q-carousel
      swipeable
      animated
      v-model="slide"
      thumbnails
      infinite
      >
      <q-carousel-slide  v-for="(pro_image_url,indx) in Product_Image_URL"
      v-bind:key="indx"
      :name="indx"
      :img-src="pro_image_url"/>
    </q-carousel>
      </div>

      <div class="col-12 col-md-4" >

        <div class="q-pa-md q-gutter-sm">
              <big><p class="caption"> {{this.detail_product['name']}}  ,  {{this.detail_product['brand']}}</p></big>
        </div>

     <div class="q-pa-md q-gutter-sm">
       <big style="color:orange;"> S$ {{this.detail_product['price']}}  </big>
      </div>

      <div class="q-pa-md">
       <q-field bottom-slots stack-label style="width:160px;">

        <template v-slot:before>
          <q-btn align="left" color="white" text-color="black"  label="-" @click="Decrease_Quantity()"/>
        </template>

        <template v-slot:control>
          <div class="self-center" style="padding-left:20px;">{{quantity_product}}</div>
        </template>

        <template v-slot:after>
          <q-btn color="white" text-color="black" label="+" @click="Increase_Quantity()"/>
        </template>

      </q-field>
    </div>

    <div class="q-pa-md q-gutter-sm">
    <q-btn outline color="primary" label="Add to cart" style="width:160px;" icon="shopping_cart"  @click="Add_To_Cart()"/>
    </div>

    </div>
  </div>
</div>
</q-page>
      <router-view />
    </q-page-container>

    <q-footer elevated class="bg-grey-8 text-white">
      <q-toolbar>
        <q-toolbar-title>
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo/svg/quasar-logo.svg">
          </q-avatar>
          Title
        </q-toolbar-title>
      </q-toolbar>
    </q-footer>

  </q-layout>
</template>
<script>

import axios from 'axios'
import VueSession from 'vue-session'
import Vue from 'vue'
// Firebase App (the core Firebase SDK) is always required and must be listed first
import * as firebase from 'firebase/app'
import 'firebase/storage'
Vue.use(VueSession)

export default {
  data () {
    return {
      left: false,
      right: false,
      slide: 1,
      detail_product: {
        price: null,
        error_type: null,
        name: null,
        image_count: 0,
        detail: null,
        id: null,
        category: null,
        brand: null,
        status: null
      },
      Product_Image_URL: [],
      quantity_product: 1,
      quantity_unique_product: 0,
      User_Cart: [],
      DETAIL_PRODUCT_ID: null
    }
  },
  beforeMount () {
    // ADD to CART
    if (this.$session.has('User_Cart')) {
      this.User_Cart = this.$session.get('User_Cart')
      this.quantity_unique_product = this.User_Cart.length
    }
    this.DETAIL_PRODUCT_ID = this.$session.get('detail_product_id')
    // this.$session.remove('detail_product_id')
    axios.get('http://localhost:8080/Detail_Product', {
      params: {
        product_detail_id: this.DETAIL_PRODUCT_ID
      }
    }).then(response => {
      this.backend_data = JSON.stringify(response.data)
      this.backend_data = JSON.parse(this.backend_data)

      if (this.backend_data.status === 'success') {
        this.detail_product = this.backend_data
        var firebaseConfig = {
          apiKey: 'AIzaSyDrb0WAPwIvOvzZxjISo1H_d33gRuz6WVM',
          authDomain: 'ecommerce-907bd.firebaseapp.com',
          databaseURL: 'https://ecommerce-907bd.firebaseio.com',
          projectId: 'ecommerce-907bd',
          storageBucket: 'ecommerce-907bd.appspot.com'
        }
        if (!firebase.apps.length) {
          firebase.initializeApp(firebaseConfig)
        }
        var PRODUCT_URL_LIST_TEMP = []
        for (var i = 0; i < this.detail_product.image_count; i++) {
          var PRODUCT_IMAGE_PATH = this.detail_product.id + '_' + i
          // Create a root reference
          var storage = firebase.storage()
          var storageRef = storage.ref()
          storageRef.child('product_images/' + PRODUCT_IMAGE_PATH).getDownloadURL().then(function (url) {
            var xhr = new XMLHttpRequest()
            xhr.responseType = 'blob'
            xhr.onload = function (event) {
            }
            xhr.open('GET', url)
            xhr.send()
            PRODUCT_URL_LIST_TEMP.push(url)
          })
        }
        this.Product_Image_URL = PRODUCT_URL_LIST_TEMP
      }
    }).catch(e => {
      console.log('Error... ')
    })
  },
  methods: {
    Show_Shopping_Cart () {
      if (this.$session.has('user_name')) {
        if (this.$session.has('User_Cart')) {
          this.$router.push('/Shopping_Card_List')
        } else {
          this.$q.notify({
            color: 'brown',
            message: 'No Shopping Cart!'
          })
        }
      } else {
        this.$q.notify({
          icon: 'done',
          color: 'negative',
          message: 'Please! LogIn!'
        })
      }
    },
    Search_Product () {
    },
    Increase_Quantity () {
      axios.get('http://localhost:8080/Product_Name', {
        params: {
          product_id: this.DETAIL_PRODUCT_ID
        }
      }).then(response => {
        this.backend_data = JSON.stringify(response.data)
        this.backend_data = JSON.parse(this.backend_data)
        if (this.backend_data.status === 'success') {
          if (this.backend_data.quantity > this.quantity_product) {
            this.quantity_product = this.quantity_product + 1
          } else {
            this.$q.notify({
              color: 'brown',
              message: 'Maximum Order Quantity : ' + this.quantity_product
            })
          }
        } else if (this.backend_data.status === 'fail') {
          console.log('Fail Respond')
        }
      }).catch(e => {
        console.log('Error... ')
      })
    },
    Decrease_Quantity () {
      if (this.quantity_product > 1) {
        this.quantity_product = this.quantity_product - 1
      } else {
        this.$q.notify({
          color: 'brown',
          message: 'Minimum Quantity: 1 '
        })
      }
    },
    Add_To_Cart () {
      if (this.$session.has('user_name')) {
        if (this.$session.has('User_Cart')) {
          const PRODUCT_ID = this.DETAIL_PRODUCT_ID
          const QUANTITY = this.quantity_product
          axios.get('http://localhost:8080/Product_Name', {
            params: {
              product_id: PRODUCT_ID
            }
          }).then(response => {
            this.backend_data = JSON.stringify(response.data)
            this.backend_data = JSON.parse(this.backend_data)
            if (this.backend_data.status === 'success') {
              var PRODUCT_NAME = this.backend_data.name
              var PRODUCT_PRICE = this.backend_data.price
              const SELECT_PRODUCT = {}
              SELECT_PRODUCT.Product_ID = PRODUCT_ID
              SELECT_PRODUCT.Product_Quantity = QUANTITY
              SELECT_PRODUCT.Product_Name = PRODUCT_NAME
              SELECT_PRODUCT.Product_Price = PRODUCT_PRICE
              this.User_Cart = this.$session.get('User_Cart')
              this.User_Cart.push(SELECT_PRODUCT)
              this.quantity_unique_product = this.User_Cart.length
              this.$session.set('User_Cart', this.User_Cart)
              this.$q.notify({
                color: 'brown',
                message: 'Added to Cart!'
              })
            } else if (this.backend_data.status === 'fail') {
              console.log('Fail Respond')
            }
          }).catch(e => {
            console.log('Error... ')
          })
        } else {
          const PRODUCT_ID = this.DETAIL_PRODUCT_ID
          const QUANTITY = this.quantity_product
          axios.get('http://localhost:8080/Product_Name', {
            params: {
              product_id: PRODUCT_ID
            }
          }).then(response => {
            this.backend_data = JSON.stringify(response.data)
            this.backend_data = JSON.parse(this.backend_data)
            if (this.backend_data.status === 'success') {
              var PRODUCT_NAME = this.backend_data.name
              var PRODUCT_PRICE = this.backend_data.price
              const SELECT_PRODUCT = {}
              SELECT_PRODUCT.Product_ID = PRODUCT_ID
              SELECT_PRODUCT.Product_Quantity = QUANTITY
              SELECT_PRODUCT.Product_Name = PRODUCT_NAME
              SELECT_PRODUCT.Product_Price = PRODUCT_PRICE
              this.User_Cart.push(SELECT_PRODUCT)
              this.quantity_unique_product = this.User_Cart.length
              this.$session.set('User_Cart', this.User_Cart)
              this.Update_Shopping_Cart()
              this.$q.notify({
                color: 'brown',
                message: 'Added to Cart!'
              })
            } else if (this.backend_data.status === 'fail') {
              console.log('Fail Respond')
            }
          }).catch(e => {
            console.log('Error... ')
          })
        }
      } else {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Please Log In!'
        })
      }
    },
    LogOut () {
      this.$session.destroy()
      this.$router.push('/')
    }
  },
  mounted: function () {
    if (this.$session.has('account_name')) {
      this.is_session_exist = true
      document.getElementById('login_account_name_label_id').innerHTML = this.$session.get('account_name')
      document.getElementById('login_user_name_label_id').textContent = this.$session.get('user_name')
    }
  }
}

</script>
<style>
</style>
