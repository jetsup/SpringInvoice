<template>
  <div class="modal flex">
    <div class="modal-content">
      <p>Are you sure you want to exit? Your changes will not be saved.</p>
      <div class="action flex">
        <button class="purple" @click="cancelModal">Cancel</button>
        <button class="red" @click="discardInvoice">Discard</button>
      </div>
    </div>
  </div>
</template>

<script>
import {mapMutations, mapState} from "vuex";

export default {
  name: "ModalComponent",

  computed: {
    ...mapState(["edit"]),
  },

  methods: {
    ...mapMutations(["TOGGLE_INVOICE_MODAL", "TOGGLE_MODAL", "TOGGLE_EDIT"]),

    cancelModal() {
      this.TOGGLE_MODAL();
    },

    discardInvoice() {
      this.TOGGLE_INVOICE_MODAL();
      this.TOGGLE_MODAL();
      if (this.edit) {
        this.TOGGLE_EDIT();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.modal {
  z-index: 100;
  position: fixed;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;

  .modal-content {
    position: relative;
    right: 10%;
    border-radius: 20px;
    padding: 24px 16px;
    max-width: 350px;
    background-color: #252945;
    color: #fff;

    p {
      text-align: center;
    }

    .action {
      justify-content: space-around;
      margin-top: 20px;
    }
  }
}
</style>
