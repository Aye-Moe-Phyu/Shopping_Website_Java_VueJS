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
      <q-btn round color="primary" icon="shopping_cart"  @click="Show_Shopping_Cart()">
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
  <div class="row">
  <div v-for="one_product in list_of_product" v-bind:key="one_product.id">
  <!--<div class="col-4" style="width:250px;height:250px;" v-on:click="Detail_Product(one_product.id)">
    <div class="row" style="width:150px;height:150px;margin:0 auto;">
    <img class="responsive"  :src="Set_Up_One_Image(one_product.id)" :id="one_product.id" />
    </div>
    <div class="row">
    <p style="padding-left:20px;font-size:18px;">{{one_product.name}} , {{one_product.brand}}</p>
    </div>
    <div class="row">
    <p style="padding-left:20px;font-size:20px;color:orange;">${{one_product.price}}</p>
    </div>
  </div> -->
  <div class="col-4"  v-on:click="Detail_Product(one_product.id)">
  <div class="q-pa-md row items-start q-gutter-md" v-on:click="Detail_Product(one_product.id)">
   <q-card style="width:250px;height:300px;">
      <img
      :src="Set_Up_One_Image(one_product.id)" :id="one_product.id" style="height:150px;"/>
      <q-card-section>
        <div class="row no-wrap items-center">
          <div class="col text-h6 ellipsis">
            {{one_product.name}}
          </div>
        </div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        <div class="text-subtitle1">
          $ {{one_product.price}}
        </div>
      </q-card-section>
    </q-card>
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
      list_of_product: [],
      quantity_unique_product: 0,
      User_Cart: [],
      DETAIL_PRODUCT_ID: null,
      text: null
    }
  },
  beforeMount () {
    axios.get('http://localhost:8080/Product_List').then(response => {
      console.log('Hit Backend URL Success...')

      this.backend_data = JSON.stringify(response.data)
      this.backend_data = JSON.parse(this.backend_data)

      if (this.backend_data.status === 'success') {
        console.log('BackEnd Data Exist!')
        this.list_of_product = this.backend_data.Product_List
      } else if (this.backend_data.status === 'fail') {
        console.log('Err...')
      }
    }).catch(e => {
      console.log('Error... ')
      console.log(e)
    })

    if (this.$session.has('user_name')) {
      if (this.$session.has('User_Cart')) {
        this.User_Cart = this.$session.get('User_Cart')
        this.quantity_unique_product = this.User_Cart.length
        this.$session.set('User_Cart', this.User_Cart)
      }
    }
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
    Search_Product (text) {
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
      console.log('search text::: ' + text)
      axios.get('http://localhost:8080/Search_Product', {
        params: {
          search_product: text
        }
      }).then(response => {
        console.log('Success...')
        this.backend_data = JSON.stringify(response.data)
        this.backend_data = JSON.parse(this.backend_data)
        console.log(this.backend_data.status)
        if (this.backend_data.status === 'success') {
          console.log('Search Data Exist!')
          this.list_of_product = this.backend_data.Product_List
        } else if (this.backend_data.status === 'fail') {
          console.log('Err...')
        }
      }).catch(e => {
        console.log('Error... ')
      })
    },
    Detail_Product (DIV_PRO_ID) {
      this.$session.set('detail_product_id', DIV_PRO_ID)
      this.$router.push('/product_detail')
    },
    Set_Up_One_Image (IMAGE_ID) {
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
      var PRODUCT_IMAGE_PATH = IMAGE_ID + '_0'
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
        // Or inserted into an <img> element:
        var img = document.getElementById(IMAGE_ID)
        img.src = url
      })
    },
    LogOut () {
      this.$session.clear()
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
<style lang="sass" scoped>
.my-card
  width: 100%
  max-width: 300px
</style>
