<template>
  <q-layout view="hHh LpR fFf">
    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="left = !left"/>
        <q-toolbar-title>
          <q-avatar>
             <img src="https://cdn.quasar.dev/img/boy-avatar.png">
          </q-avatar>
          Senze Hub
        </q-toolbar-title>
        <q-btn dense flat round icon="menu" @click="right = !right" />
      </q-toolbar>
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
          <q-avatar color="blue" icon="account_circle" id="login_user_icon_id">
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
      <q-separator />

    </q-list>
    </div>
    </q-drawer>
    <q-drawer show-if-above v-model="right" side="right" bordered>
    <!-- drawer content -->
    </q-drawer>
    <q-page-container>

<q-page class="flex flex-center">
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
Vue.use(VueSession)

export default {
  data () {
    return {
      left: false,
      right: false
    }
  },
  mounted: function () {
    if (this.$session.has('account_name')) {
      this.is_session_exist = true
      document.getElementById('login_account_name_label_id').innerHTML = this.$session.get('account_name')
      document.getElementById('login_user_name_label_id').textContent = this.$session.get('user_name')
    }
  },
  methods: {
    LogOut () {
      this.$session.destroy()
      this.$router.go()
    }
  }
}
</script>

<style>
</style>
