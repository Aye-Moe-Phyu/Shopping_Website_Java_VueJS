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
    <q-list bordered style="width:280px;">
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
        <span id="login_user_name_label_id" style="color:black;"> Log in </span>
        <span id="login_account_name_label_id" style="color:black;"></span>
        </q-item-section>
      </q-item>
      <q-separator />

    </q-list>
    </div>
    </q-drawer>
    <q-drawer show-if-above v-model="right" side="right" bordered>
    <!-- drawer content -->
    </q-drawer>
    <q-page-container>
    <q-page class="flex flex-center">
   <div class="q-pa-md" style="width:300px;">
    <form @submit.prevent.stop="onSubmit" @reset.prevent.stop="onReset" class="q-gutter-md">
      <q-input filled
      v-model.trim="email"
      @input="$v.email.$touch()"
      label="Email *"
      hint="ayemoephyu151996@gmail.com"
      :rules="[ val => $v.email.required || 'Email is required', val => $v.email.email || 'Invalid email format']"
      />

      <q-input
        ref="password"
        filled
        type="password"
        v-model="password"
        label="Password *"
        hint="secure_password"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Password is required']"
      />
      <div>
        <q-btn label="Submit" type="submit" color="primary" />
        <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
      </div>
    </form>
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
import { required, email } from 'vuelidate/lib/validators'
import axios from 'axios'
import VueSession from 'vue-session'
import Vue from 'vue'
import VueSimpleAlert from 'vue-simple-alert'

Vue.use(VueSimpleAlert)
Vue.use(VueSession)
export default {
  data () {
    return {
      backend_data: [],
      email: null,
      password: null,
      left: false,
      right: false,
      accept: false,
      quantity_unique_product: 0
    }
  },
  validations: {
    email: {
      required,
      email
    },
    password: {
      required
    }
  },
  beforeCreate: function () {
    if (this.$session.has('account_name')) {
      this.$router.push('/user_account_info')
    }
  },
  methods: {
    Show_Shopping_Cart () {
      if (this.$session.has('user_name')) {
        this.$router.push('/Shopping_Card_List')
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
    onSubmit () {
      if (this.email === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Email Box'
        })
      } else if (this.password === null) {
        this.$q.notify({
          icon: 'clear',
          color: 'negative',
          message: 'Empty Password Box'
        })
      } else {
        // alert('Send Data to Backend!')
        this.$q.notify({
          icon: 'done',
          color: 'positive',
          message: 'Submitted'
        })
        axios.get('http://localhost:8080/Sign_In', {
          params: {
            user_mail: this.email,
            user_password: this.password
          }
        }).then(response => {
          console.log('Success...')
          this.backend_data = JSON.stringify(response.data)
          this.backend_data = JSON.parse(this.backend_data)

          if (this.backend_data.status === 'success') {
            // Create Session
            this.$session.start()
            this.$session.set('user_name', this.backend_data.name)
            this.$session.set('account_name', this.backend_data.mail)
            // Crate Image Logo
            document.getElementById('login_account_name_label_id').innerHTML = this.$session.get('account_name')
            document.getElementById('login_user_name_label_id').innerHTML = this.$session.get('user_name')
            this.$router.push('/')
          } else if (this.backend_data.status === 'fail') {
            // No User Error
            if (this.backend_data.error_type === 'no_user') {
              this.$alert('No User Found!')
            } else if (this.backend_data.error_type === 'wrong_password') {
              this.$alert('Wrong Password!')
            }
          }

          console.log(this.backend_data.status)
        }).catch(e => {
          console.log('Error... ')
        })
      }
    },
    onReset () {
      this.email = null
      this.password = null
    }
  }
}

</script>

<style>
</style>
