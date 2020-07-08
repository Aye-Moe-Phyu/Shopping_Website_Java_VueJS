<template>
  <q-layout view="hHh LpR fFf">
    <q-header elevated class="bg-primary text-white">
       <div class="q-pa-md">
    <div class="row no-wrap">
      <q-toolbar class="col-7">
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
      </div>
      </div>
    </q-header>
    <q-drawer show-if-above v-model="left" side="left" bordered>
    <!-- drawer content -->
    <div class="q-pa-md" >
    <q-list bordered style="width:280px;" id="q_list_id">
      <q-item clickable v-ripple link to="/" >
        <q-item-section avatar>
          <q-avatar color="blue" text-color="white" icon="home" />
        </q-item-section>
        <q-item-section style="color:black;">Home</q-item-section>
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
    <q-drawer show-if-above v-model="right" side="right" bordered>
    <!-- drawer content -->
    </q-drawer>
    <q-page-container>

   <!-- Columns are always 50% wide, on mobile and desktop -->
  <form  @submit.prevent="uploadFile" @submit.prevent.stop="onSubmit" @reset.prevent.stop="onReset" class="q-gutter-md" >

    <!-- Product Name Row -->
    <div class="row" >
      <div class="col-3" style="text-align:center;vertical-align:middle;line-height:50px;">
        Name
      </div>
      <div class="col-9" style="margin:0 auto;">
      <q-input filled
      v-model.trim="product_name"
      @input="$v.product_name.$touch()"
      :rules="[ val => $v.product_name.required || 'product_name is required']"
      />
      </div>
    </div>
    <!-- Product Brand Row -->
    <div class="row" >
      <div class="col-3" style="text-align:center;vertical-align:middle;line-height:50px;">
        Brand
      </div>
      <div class="col-9" style="margin:0 auto;">
      <q-input filled
      v-model.trim="product_brand"
      @input="$v.product_brand.$touch()"
      :rules="[ val => $v.product_brand.required || 'product_brand is required']"
      />
      </div>
    </div>
    <!-- Product Details Row -->
    <div class="row" >
      <div class="col-3" style="text-align:center;vertical-align:middle;line-height:50px;">
        Details
      </div>
      <div class="col-9" style="margin:0 auto;">
      <q-input filled
      type="textarea" color="tertiary" :rows="5"
      v-model.trim="product_details"
      @input="$v.product_details.$touch()"
      :rules="[ val => $v.product_details.required || 'product_details is required']"
      />
      </div>
    </div>

    <!-- Product Category Row -->
    <div class="row" >
      <div class="col-3" style="text-align:center;vertical-align:middle;line-height:50px;">
        Category
      </div>
      <div class="col-9">
      <div class="q-gutter-md" style="max-width: 300px;text-align:center;">
      <q-select v-model="product_category" :options="options"/>
      </div>
      </div>
    </div>

    <!-- Product Quantity Row -->
    <div class="row" >
      <div class="col-3" style="text-align:center;vertical-align:middle;line-height:50px;">
        Quantity
      </div>
      <div class="col-9" style="margin:0 auto;">
      <q-input filled
      v-model.trim="product_quantity"
      type="number"
      @input="$v.product_quantity.$touch()"
      :rules="[ val => $v.product_quantity.required || 'product_quantity is required']"
      />
      </div>
    </div>

    <!-- Product Price Row -->
    <div class="row" >
      <div class="col-3" style="text-align:center;vertical-align:middle;line-height:50px;">
        Price
      </div>
      <div class="col-9" style="margin:0 auto;">
      <q-input filled
      v-model.trim="product_price"
      type="number"
      @input="$v.product_price.$touch()"
      :rules="[ val => $v.product_price.required || 'product_brand is required']"
      />
      </div>
    </div>

    <!-- Product Image Row -->
    <div class="row" >
      <div class="col-3" style="text-align:center;vertical-align:middle;line-height:50px;">
        Sample Image
      </div>
      <div class="col-9" style="margin:0 auto;">
      <div class="q-gutter-md" style="max-width: 300px">
      <q-uploader
      hide-upload-btn
      :auto-expand='true'
      :multiple='true'
      float-label ="Select images and videos you want to share by clicking on plus icon &#8688;"
      ref="files"
      :upload-factory="uploadFile"
      inverted
      />
      </div>
      </div>
    </div>

    <!-- Submit Button Row -->
    <div class="row" >
      <div class="col-9">
      </div>
      <div class="col-3" style="margin:0 auto;">
      <q-btn label="Submit" type="submit" color="primary" />
      <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
      </div>
    </div>

    </form>
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
import { required } from 'vuelidate/lib/validators'
import axios from 'axios'
import VueSession from 'vue-session'
import Vue from 'vue'
import VueSimpleAlert from 'vue-simple-alert'
// Firebase App (the core Firebase SDK) is always required and must be listed first
import * as firebase from 'firebase/app'
import 'firebase/storage'
Vue.use(VueSimpleAlert)
Vue.use(VueSession)

export default {
  data () {
    return {
      left: false,
      right: false,
      product_id: null,
      product_name: null,
      product_brand: null,
      product_details: null,
      product_category: null,
      product_price: null,
      product_quantity: null,
      product_image_count: 0,
      options: [
        'Phone', 'Laptop', 'TV', 'Earphone', 'Shoe'
      ],
      product_formdata: null
    }
  },
  validations: {
    product_name: {
      required
    },
    product_quantity: {
      required
    },
    product_price: {
      required
    },
    product_brand: {
      required
    },
    product_details: {
      required
    },
    product_category: {
      required
    },
    product_image: {
      required
    }
  },
  methods: {
    uploadFile: function (file, updateProgress) {
      if (this.product_name === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Product Name'
        })
      } else if (this.product_brand === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Product Brand'
        })
      } else if (this.product_details === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Product Detail'
        })
      } else if (this.product_category === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Product Category'
        })
      } else if (this.product_image === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Product Image'
        })
      } else if (this.product_quantity === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Product Quantity'
        })
      } else if (this.product_price === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Product Brand'
        })
      } else {
        // alert('Send Data to Backend!')
        // console.log('--- Uploading File ---')
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

        // console.log('--- UPLOAD FILE ---')
        const uuidv4 = require('uuid/v4')
        const Uniquenumber = uuidv4()

        // Create a root reference
        var storage = firebase.storage()
        var storageRef = storage.ref('/product_images')

        this.$q.notify({
          icon: 'done',
          color: 'positive',
          message: 'Submitted'
        })

        const formData = new FormData()

        const files = this.$refs.files.files.concat(file)
        for (var i = 0; i < files.length - 1; i++) {
          const file = files[i]
          // Create a reference to 'images.jpg'
          var ProductRef = storageRef.child(Uniquenumber + '_' + i)

          ProductRef.put(file).then(function (snapshot) {
            console.log('Upload Image')
          })
          this.product_image_count = this.product_image_count + 1
        }
        // console.log('Quantity of Image: ' + this.product_image_count)
        this.product_id = Uniquenumber
        formData.set('product_image_count', this.product_image_count)
        formData.set('product_id', this.product_id)
        formData.set('product_name', this.product_name)
        formData.set('product_detail', this.product_details)
        formData.set('product_brand', this.product_brand)
        formData.set('product_category', this.product_category)
        formData.set('product_price', this.product_price)
        formData.set('product_quantity', this.product_quantity)
        this.product_formdata = formData
        this.$router.push('/')
      }
    },
    LogOut () {
      this.$session.destroy()
      this.$router.go()
    },
    onSubmit () {
      var headers = {
        'Content-Type': 'multipart/form-data'
      }
      axios.post('http://localhost:8080/Create_Product', this.product_formdata, headers).then(
        function () {
          console.log('SUCCESS!!')
        })
        .catch(function () {
          console.log('FAILURE!!')
        })
    },
    onReset () {
      this.email = null
      this.password = null
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
