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

<div class="row" v-for="(data,index) in this.User_Cart"  v-bind:key="data.Product_ID">

<div class="col-2" style="text-align:right;">
    <img class="responsive" style="height:80px;" :src="Set_Up_One_Image(data.Product_ID,index)" :id="data.Product_ID+'_'+index" />
</div>
<div class="col-3" style="text-align:center;">
<big>
  {{data.Product_Name}}
</big>
</div>
<div class="col-2" style="text-align:center;">
<big style="color:black;"> $ {{data.Product_Price}}
</big>
</div>
<div class="col-2" style="text-align:left;">
   <div class="q-pa-md">
       <q-field bottom-slots stack-label style="width:160px;">

        <template v-slot:before>
          <q-btn align="left" color="white" text-color="black"  label="-" @click="Decrease_Quantity(index)"/>
        </template>

        <template v-slot:control>
          <div class="self-center no-outline" style="padding-left:20px;">
          <big>{{data.Product_Quantity}}</big>
          </div>
        </template>

        <template v-slot:after>
          <q-btn color="white" text-color="black" label="+" @click="Increase_Quantity(index)"/>
        </template>

      </q-field>
    </div>
</div>
<div class="col-2" style="text-align:center;">
<big style="color:orange;"> $ {{data.Product_Price * data.Product_Quantity}}
</big>
</div>
<div class="col-1" style="text-align:left;">
  <q-btn color="gray" text-color="black" glossy label="Delete" @click="Delete(data.Product_ID)" />
</div>
</div>
<q-separator inset="item-thumbnail" />
<div class="row">
    <div class="col-9" style="text-align:right;padding:40px">
       <big> Total Cost</big>
    </div>
    <div class="col-3" style="text-align:center;padding:40px">
        <big style="color:orange;"> S$ {{GET_TOTAL()}}
        </big>
    </div>
</div>
<div class="row">
<div class="col-8">
</div>
<div class="col-4">
<drag-verify :width="300" :height="60" text="Check Out"  success-text="Thanks" background="#33AFFF" progress-bar-bg="#3CA9F0"
        completed-bg="#3E74E1" handler-bg="white"
        handler-icon="handlerIcon"
        text-size="textSize"
        success-icon="successIcon"
        :circle="true">
</drag-verify>
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
import VueSession from 'vue-session'
import Vue from 'vue'
// Firebase App (the core Firebase SDK) is always required and must be listed first
import * as firebase from 'firebase/app'
import 'firebase/database'
import axios from 'axios'
import 'firebase/storage'
import dragVerify from './dragVerify'
Vue.use(VueSession)

export default {
  components: {
    dragVerify
  },
  data () {
    return {
      left: false,
      right: false,
      list_of_product: [],
      quantity_unique_product: 0,
      User_Cart: [],
      quality_product: 0,
      Product_ID_and_NAME: [],
      backend_data: null,
      TEMP_JSON_DATA: {},
      basicModel: 50
    }
  },
  beforeMount () {
    // User Cart Process
    if (this.$session.has('user_name')) {
      if (this.$session.has('User_Cart')) {
        this.User_Cart = this.$session.get('User_Cart')
        this.quantity_unique_product = this.User_Cart.length
        this.$session.set('User_Cart', this.User_Cart)
      }
    }
  },
  methods: {
    GET_TOTAL () {
      var TOTAL = 0
      for (var i = 0; i < this.User_Cart.length; i++) {
        TOTAL += this.User_Cart[i].Product_Quantity * this.User_Cart[i].Product_Price
      }
      return TOTAL
    },
    Increase_Quantity (INDEX) {
      axios.get('http://localhost:8080/Product_Name', {
        params: {
          product_id: this.User_Cart[INDEX].Product_ID
        }
      }).then(response => {
        this.backend_data = JSON.stringify(response.data)
        this.backend_data = JSON.parse(this.backend_data)
        if (this.backend_data.status === 'success') {
          if (this.backend_data.quantity > this.User_Cart[INDEX].Product_Quantity) {
            this.User_Cart[INDEX].Product_Quantity = this.User_Cart[INDEX].Product_Quantity + 1
            this.$session.set('User_Cart', this.User_Cart)
          } else {
            this.$q.notify({
              color: 'brown',
              message: 'Maximum Order Quantity : ' + this.User_Cart[INDEX].Product_Quantity
            })
          }
        } else if (this.backend_data.status === 'fail') {
          console.log('Fail Respond')
        }
      }).catch(e => {
        console.log('Error... ')
      })
    },
    Decrease_Quantity (INDEX) {
      if (this.User_Cart[INDEX].Product_Quantity > 1) {
        this.User_Cart[INDEX].Product_Quantity = this.User_Cart[INDEX].Product_Quantity - 1
        this.$session.set('User_Cart', this.User_Cart)
      } else {
        this.$q.notify({
          color: 'brown',
          message: 'Minimum Quantity : 1'
        })
      }
    },
    Show_Shopping_Cart () {
      var path = '/Shopping_Card_List'
      if (this.$route.path !== path) this.$router.push(path)
    },
    Search_Product () {
      alert('Search Product!')
    },
    Set_Up_One_Image (IMAGE_ID, INDEX) {
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
      console.log('Image Name: ' + PRODUCT_IMAGE_PATH)
      // Create a root reference
      var storage = firebase.storage()
      var storageRef = storage.ref()
      storageRef.child('product_images/' + PRODUCT_IMAGE_PATH).getDownloadURL().then(function (url) {
        var xhr = new XMLHttpRequest()
        xhr.responseType = 'blob'
        xhr.onload = function (event) {
          var blob = xhr.response
          console.log(blob)
        }
        xhr.open('GET', url)
        xhr.send()
        // Or inserted into an <img> element:
        var img = document.getElementById(IMAGE_ID + '_' + INDEX)
        img.src = url
      })
    },
    LogOut () {
      this.$session.clear()
      this.$router.push('/')
    },
    Delete (PRD_ID) {
      this.$q.dialog({
        title: 'Confirm',
        message: 'Do you want to delete?',
        cancel: true,
        persistent: true
      }).onOk(() => {
        for (var i = 0; i < this.User_Cart.length; i++) {
          if (this.User_Cart[i].Product_ID === PRD_ID) {
            alert('Success!')
            alert(this.User_Cart[i].Product_ID)
            const index = i
            if (index > -1) {
              this.User_Cart.splice(index, 1)
            }
          }
        }
        alert(this.User_Cart.length)
        this.$session.set('User_Cart', this.User_Cart)
        // console.log('>>>> OK')
      }).onCancel(() => {
        // console.log('>>>> Cancel')
      })
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
