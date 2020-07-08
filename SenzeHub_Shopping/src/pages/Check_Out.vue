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
  <div class="row">
    <div class="justify-center full-height full-width text-center">
     <big>
       <b>
       One Time Payment
       </b>
       </big>
    </div>
  </div>

<div class='credit-card-inputs' :class='{ complete }'>
    <!-- Card Number Row -->
    <div class="row" style="padding-top:30px;padding:10px;">
      <div class="col-5">
        <div class="justify-center full-height full-width text-center">
        <b> Credit Card Info </b>
        </div>
      </div>
      <div class="col-7">
      <card-number class='stripe-element card-number'
      ref='cardNumber'
      :stripe='stripe'
      style="background-color:#E8E8E8;width:200px;height:30px;padding-left:15px;"
      @change='number = $event.complete'
    />
      </div>
    </div>

    <!-- Card CVC Row -->
    <div class="row" style="padding-top:30px;padding:10px;" >
      <div class="col-5" style="text-align:center;vertical-align:middle;line-height:50px;">
        <b> CVC </b>
      </div>
      <div class="col-7" style="margin:0 auto;">
    <card-cvc class='stripe-element card-cvc'
      ref='cardCvc'
      :stripe='stripe'
      @change='cvc = $event.complete'
      style="background-color:#E8E8E8;width:200px;height:30px;padding-left:15px;"
    />
      </div>
    </div>
    <!-- Card Expiry Date Row -->
    <div class="row"  style="padding-top:30px;padding:10px;" >
      <div class="col-5" style="text-align:center;vertical-align:middle;line-height:50px;">
        <b> Expiry Date </b>
      </div>
      <div class="col-7" style="margin:0 auto;">
      <card-expiry class='stripe-element card-expiry'
      ref='cardExpiry'
      :stripe='stripe'
      @change='expiry = $event.complete'
      style="background-color:#E8E8E8;width:200px;height:30px;padding-left:15px;"
    />
      </div>
    </div>
    <!-- Submit Button Row -->
    <div class="row" >
      <div class="col-5">
      </div>
      <div class="col-7" style="margin:0 auto;">
        <q-btn color="primary" label="Pay with credit card" class='pay-with-stripe' @click='pay' />
      </div>
    </div>
</div>
<q-dialog v-model="small">
      <q-card style="width: 300px">
        <q-card-section>
          <div class="text-h6">Thanks</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          Payment Success.....
        </q-card-section>
        <q-card-actions align="right" class="bg-white text-teal">
          <q-btn flat label="OK" v-close-popup @click="Home"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
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
import { CardNumber, CardExpiry, CardCvc, createToken } from 'vue-stripe-elements-plus'
import { required } from 'vuelidate/lib/validators'
import axios from 'axios'
import VueSession from 'vue-session'
import Vue from 'vue'
// Firebase App (the core Firebase SDK) is always required and must be listed first
import * as firebase from 'firebase/app'
import 'firebase/storage'
Vue.use(VueSession)

export default {
  components: { CardNumber, CardExpiry, CardCvc },
  data () {
    return {
      left: false,
      right: false,
      quantity_unique_product: 0,
      stripe: 'pk_test_51GvJJbDyK93lH1aLDmEsGoXW0BdskjHO6UgF3q0oz9Vq2UgIGJ6jpibbKd7Dr5l3nV9MEzQ76IAvmTj7c9IU6EgA00xUnz0K0g',
      complete: false,
      number: false,
      expiry: false,
      cvc: false,
      small: false
    }
  },
  validations: {
    card_number: {
      required
    },
    card_cvc: {
      required
    },
    card_expiry_date: {
      required
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
    this.$refs.modalRef.hide()
  },
  methods: {
    GET_TOTAL () {
      var TOTAL = 0
      for (var i = 0; i < this.User_Cart.length; i++) {
        TOTAL += this.User_Cart[i].Product_Quantity * this.User_Cart[i].Product_Price
      }
      return TOTAL
    },
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
      alert('Search Product!')
    },
    Detail_Product (DIV_PRO_ID) {
      this.$session.set('detail_product_id', DIV_PRO_ID)
      this.$router.push('/product_detail')
    },
    Set_Up_One_Image (IMAGE_ID) {
      var firebaseConfig = {
        apiKey: 'AIzaSyA5UcHMkGOR828dYWtQgsGKsbLbG4-Vf5Q',
        authDomain: 'shopping-online-ac432.firebaseapp.com',
        databaseURL: 'https://shopping-online-ac432.firebaseio.com',
        projectId: 'shopping-online-ac432',
        storageBucket: 'shopping-online-ac432.appspot.com'
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
    },
    Home () {
      this.$router.push('/')
    },
    update () {
      this.complete = this.number && this.expiry && this.cvc
      // field completed, find field to focus next
      if (this.number) {
        if (!this.expiry) {
          this.$refs.cardExpiry.focus()
        } else if (!this.cvc) {
          this.$refs.cardCvc.focus()
        }
      } else if (this.expiry) {
        if (!this.cvc) {
          this.$refs.cardCvc.focus()
        } else if (!this.number) {
          this.$refs.cardNumber.focus()
        }
      }
      // no focus magic for the CVC field as it gets complete with three
      // numbers, but can also have four
    },
    pay () {
      if (!this.number && !this.cvc && !this.expiry) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Oop! Something Missing!'
        })
      } else if (!this.number) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Card Number'
        })
      } else if (!this.cvc) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Card CVC'
        })
      } else if (!this.expiry) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Card Expiry Date'
        })
      } else {
        // createToken returns a Promise which resolves in a result object with
        // either a token or an error key.
        // See https://stripe.com/docs/api#tokens for the token object.
        // See https://stripe.com/docs/api#errors for the error object.
        // More general https://stripe.com/docs/stripe.js#stripe-create-token.
        createToken().then(data => {
          alert(data.token.id)
          axios.get('http://localhost:8080/Charge', {
            params: {
              token: data.token.id,
              amount: this.GET_TOTAL()
            }
          }).then(response => {
            console.log('Success...')
            this.$session.remove('User_Cart')
            this.quantity_unique_product = 0
            this.small = true
          }).catch(e => {
            console.log('Error... ')
          })
        })
      }
    }
  },
  watch: {
    number () { this.update() },
    expiry () { this.update() },
    cvc () { this.update() }
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
