<template>
  <div
    ref="invoiceWrapper"
    class="invoice-wrapper flex flex-column"
    @click="checkClick"
  >
    <form class="invoice-content" @submit.prevent="submitForm">
      <Loading v-show="loading" />
      <h1 v-if="!edit">New Invoice</h1>
      <h1 v-else>Edit Invoice</h1>

      <!-- Bill To -->
      <div class="bill-to flex flex-column">
        <h4>Bill To</h4>
        <div class="input flex flex-column">
          <label for="clientName">Client's Name</label>
          <input id="clientName" v-model="clientName" required type="text" />
        </div>
        <div class="input flex flex-column">
          <label for="clientEmail">Client's Email</label>
          <input id="clientEmail" v-model="clientEmail" required type="text" />
        </div>
        <div class="input flex flex-column">
          <label for="clientStreetAddress">Street Address</label>
          <input
            id="clientStreetAddress"
            v-model="clientStreetAddress"
            required
            type="text"
          />
        </div>
        <div class="location-details flex">
          <div class="input flex flex-column">
            <label for="clientCity">City</label>
            <input id="clientCity" v-model="clientCity" required type="text" />
          </div>
          <div class="input flex flex-column">
            <label for="clientZipCode">Zip Code</label>
            <input
              id="clientZipCode"
              v-model="clientZipCode"
              required
              type="text"
            />
          </div>
          <div class="input flex flex-column">
            <label for="clientCountry">Country</label>
            <input
              id="clientCountry"
              v-model="clientCountry"
              required
              type="text"
            />
          </div>
        </div>
      </div>

      <!-- Invoice Work Details -->
      <div class="invoice-work flex flex-column">
        <div class="payment flex">
          <div class="input flex flex-column">
            <label for="invoiceDate">Invoice Date</label>
            <input
              id="invoiceDate"
              v-model="invoiceDate"
              disabled
              type="text"
            />
          </div>
          <div class="input flex flex-column">
            <label for="paymentDueDate">Payment Due</label>
            <input
              id="paymentDueDate"
              v-model="paymentDueDate"
              :min="minDate"
              required
              type="date"
            />
          </div>
        </div>

        <div class="input flex flex-column">
          <label for="invoiceNotes">Notes</label>
          <input
            id="invoiceNotes"
            v-model="invoiceNotes"
            required
            type="text"
          />
        </div>

        <div class="handle-items">
          <h3>Item List</h3>
          <table class="item-table">
            <tr class="table-heading flex">
              <th class="item-name">Item Name</th>
              <th class="quantity">Quantity</th>
              <th class="price">Price</th>
              <th class="total">Total</th>
            </tr>
            <tr
              v-for="item in invoiceItemList"
              :key="item.id"
              class="table-data flex"
            >
              <td class="item-name">
                <input v-model="item.name" type="text" />
              </td>
              <td class="quantity">
                <input v-model="item.quantity" type="text" />
              </td>
              <td class="price">
                <input v-model="item.unitPrice" type="text" />
              </td>
              <td class="total flex">
                <span
                  >${{ (item.total = item.quantity * item.unitPrice) }}</span
                >
                <img
                  alt=""
                  src="../assets/icon-delete.svg"
                  @click="deleteInvoiceItem(item.id)"
                />
              </td>
            </tr>
          </table>

          <div class="flex button" @click="addInvoiceItem">
            <svg height="11" width="11" xmlns="http://www.w3.org/2000/svg">
              <path
                d="M6.313 10.023v-3.71h3.71v-2.58h-3.71V.023h-2.58v3.71H.023v2.58h3.71v3.71z"
                fill="white"
                fill-rule="nonzero"
              />
            </svg>
            Add New Item
          </div>
        </div>
      </div>

      <hr />

      <!-- Save/Discard -->
      <div class="save flex">
        <div class="left">
          <button class="red" type="button" @click="discardInvoice">
            {{ edit ? "Cancel" : "Discard" }}
          </button>
        </div>
        <div class="right flex">
          <template v-if="!edit">
            <button class="dark-purple" type="submit" @click="saveDraft">
              Save Draft
            </button>
            <button class="purple" type="submit" @click="publishInvoice">
              Create Invoice
            </button>
          </template>
          <template v-else>
            <button class="purple" type="submit">Update</button>
          </template>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
// import db from "../firebase/firebaseInit";
import {mapActions, mapMutations, mapState} from "vuex";
// import { uid } from "uid";
import Loading from "./Loading.vue";

export default {
  name: "InvoiceModal",
  components: { Loading },
  data() {
    return {
      id: null,
      clientName: null,
      clientEmail: null,
      clientStreetAddress: null,
      clientCity: null,
      clientZipCode: null,
      clientCountry: null,
      invoiceDate: null,
      invoiceNotes: null,
      paymentDueDate: null,
      invoicePending: null,
      invoiceDraft: null,
      invoiceItemList: [],
      invoiceTotal: 0,

      // other
      dateOptions: {
        weekday: "short",
        year: "numeric",
        month: "short",
        day: "numeric",
      },

      loading: null,

      minDate: null,
    };
  },

  mounted() {
    // Get the current date
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, "0");
    const dd = String(today.getDate()).padStart(2, "0");
    this.minDate = `${yyyy}-${mm}-${dd}`;
  },

  created() {
    if (!this.edit) {
      // get current date for invoice date field
      this.invoiceDateUnix = Date.now();
      this.invoiceDate = new Date(this.invoiceDateUnix).toLocaleDateString(
        "en-us",
        this.dateOptions
      );
    } else {
      const currentInvoice = this.currentInvoice;
      this.id = currentInvoice.id;
      this.clientName = currentInvoice.clientName;
      this.clientEmail = currentInvoice.clientEmail;
      this.clientStreetAddress = currentInvoice.clientStreetAddress;
      this.clientCity = currentInvoice.clientCity;
      this.clientZipCode = currentInvoice.clientZipCode;
      this.clientCountry = currentInvoice.clientCountry;
      this.invoiceDate = currentInvoice.invoiceDate;
      this.paymentDueDate = currentInvoice.paymentDueDate;
      this.invoicePending = currentInvoice.invoicePending;
      this.invoiceDraft = currentInvoice.invoiceDraft;
      this.invoiceItemList = currentInvoice.invoiceItemList;
      this.invoiceTotal = currentInvoice.invoiceTotal;
      this.invoiceNotes = currentInvoice.invoiceNotes;
    }
  },

  computed: {
    ...mapState(["edit", "currentInvoice"]),
  },

  methods: {
    ...mapMutations(["TOGGLE_INVOICE_MODAL", "TOGGLE_MODAL", "TOGGLE_EDIT"]),
    ...mapActions(["UPDATE_INVOICE", "GET_INVOICES"]),

    checkClick(e) {
      if (e.target === this.$refs.invoiceWrapper) {
        this.TOGGLE_MODAL();
      }
    },

    addInvoiceItem() {
      this.invoiceItemList.push({
        // id: uid(),
        itemName: "",
        quantity: "",
        price: "",
        total: 0,
      });
    },

    async deleteInvoiceItem(id) {
      this.invoiceItemList = this.invoiceItemList.filter(
        (item) => item.id !== id
      );

      await fetch(`http://localhost:8090/api/v1/products/${id}`, {
        method: "DELETE",
      }).then((res) => {
        // TODO: show a toast message
        if (res.ok) {
          console.log("Success");
        } else {
          console.log("Failed");
        }
      });
    },

    discardInvoice() {
      this.TOGGLE_INVOICE_MODAL();
      if (this.edit) {
        this.TOGGLE_EDIT();
      }
    },

    publishInvoice() {
      if (this.paymentDueDate === null) {
        return;
      }
      this.invoicePending = true;
      this.createInvoice();
    },

    saveDraft() {
      this.invoiceDraft = true;
      this.createInvoice();
    },

    calcInvoiceTotal() {
      this.invoiceTotal = this.invoiceItemList.reduce((sum, item) => {
        return sum + item.total;
      }, 0);
    },

    async createInvoice() {
      if (this.invoiceItemList.length === 0 && !this.invoiceDraft) {
        alert("Please ensure you have at least one item in your invoice.");
        return;
      }

      this.loading = true;

      this.calcInvoiceTotal();

      let invoiceStatus = "";
      if (this.invoicePending) {
        invoiceStatus = "PENDING";
      } else if (this.invoiceDraft) {
        invoiceStatus = "DRAFT";
      } else {
        invoiceStatus = "PAID";
      }

      await fetch(`http://localhost:8090/api/v1/invoices/new`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          invoiceStatus: invoiceStatus,
          clientName: this.clientName,
          clientEmail: this.clientEmail,
          clientStreetAddress: this.clientStreetAddress,
          clientCity: this.clientCity,
          clientZipCode: this.clientZipCode,
          clientCountry: this.clientCountry,
          dueDate: this.paymentDueDate,
          notes: this.invoiceNotes,
        }),
      })
        .then(async (invoiceResponse) => {
          const invoiceId = await invoiceResponse.json();
          // Iterate through the invoiceItemList and create the product items in the database
          for (const item of this.invoiceItemList) {
            if (
              item.name === "" ||
              item.quantity === "" ||
              item.unitPrice === ""
            ) {
              continue;
            }
            fetch(`http://localhost:8090/api/v1/products/new`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                name: item.name,
                quantity: item.quantity,
                unitPrice: item.unitPrice,
                invoiceId: invoiceId,
              }),
            }).then((r) => {
              if (!r.ok) {
                console.error("Failed to create product");
              } else {
                console.log("Product created successfully");
              }
              this.loading = false;
              // FIXME: find a better way
              window.location.reload();
            });
          }

          console.log("Invoice and products created successfully");
        })
        .finally(() => {
          this.loading = false;
          // FIXME: find a better way for this
          window.location.reload();
        });
    },

    async updateInvoice() {
      if (this.invoiceItemList.length === 0) {
        alert("Please ensure you have at least one item in your invoice.");
        return;
      }
      // asynchronously update data through http://localhost:8090/api/v1/invoices/:id
      this.loading = true;
      this.calcInvoiceTotal();

      let invoiceStatus = "";
      if (this.invoicePending) {
        invoiceStatus = "PENDING";
      } else if (this.invoiceDraft) {
        invoiceStatus = "DRAFT";
      } else {
        invoiceStatus = "PAID";
      }

      await fetch(
        `http://localhost:8090/api/v1/invoices/${this.$route.params.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            invoiceStatus: invoiceStatus,
            clientName: this.clientName,
            clientEmail: this.clientEmail,
            clientStreetAddress: this.clientStreetAddress,
            clientCity: this.clientCity,
            clientZipCode: this.clientZipCode,
            clientCountry: this.clientCountry,
            dueDate: this.paymentDueDate,
            notes: this.invoiceNotes,
          }),
        }
      ).then((res) => {
        console.log("RES:", res.ok, res.status);
        if (res.ok) {
          console.log("Success");
          // loop through the invoiceItemList and update the items but now update the product's endpoint
          // http://localhost:8090/api/v1/products/:productId
          this.invoiceItemList.forEach(async (item) => {
            if (item.id !== undefined) {
              await fetch(`http://localhost:8090/api/v1/products/${item.id}`, {
                method: "PUT",
                headers: {
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({
                  name: item.name,
                  quantity: item.quantity,
                  unitPrice: item.unitPrice,
                }),
              }).then((res) => {
                if (res.ok) {
                  console.log("Success");
                } else {
                  console.log("Failed");
                }
                this.loading = false;
              });
            } else {
              await fetch(`http://localhost:8090/api/v1/products/new`, {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({
                  name: item.name,
                  quantity: item.quantity,
                  unitPrice: item.unitPrice,
                  invoiceId: this.$route.params.id,
                }),
              }).then((res) => {
                if (res.ok) {
                  console.log("Success");
                } else {
                  console.log("Failed");
                }
                this.loading = false;
              });
            }
          });
        } else {
          console.log("Failed");
          this.loading = false;
        }
      });
    },

    submitForm() {
      if (!this.edit) {
        // this.createInvoice();
        console.log("Uploading");
      } else {
        this.updateInvoice();
      }
    },
  },

  watch: {
    paymentTerms() {
      this.paymentDueDateUnix =
        this.invoiceDateUnix + this.paymentTerms * 24 * 60 * 60 * 1000;
      this.paymentDueDate = new Date(
        this.paymentDueDateUnix
      ).toLocaleDateString("en-us", this.dateOptions);
    },
  },
};
</script>

<style lang="scss" scoped>
.invoice-wrapper {
  position: fixed;
  z-index: 1; // make it above the filter button
  top: 0;
  left: 0;
  background-color: transparent;
  width: 100%;
  height: 100vh;
  overflow: scroll;

  &::-webkit-scrollbar {
    display: none;
  }

  @media (min-width: 750px) {
    left: 90px; // navigation sidebar width
  }
}

.invoice-content {
  position: relative;
  padding: 40px 25px;
  max-width: 500px;
  width: 100%;
  color: white;
  background-color: #141625;
  box-shadow: 10px 4px 6px -1px rgba(0, 0, 0, 0.2),
    0 2px 4px -1px rgba(0, 0, 0, 0.06);

  h1 {
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 40px;
    color: white;
  }

  h3 {
    margin-bottom: 16px;
    font-size: 18px;
    color: #777f98;
  }

  h4 {
    color: #7c5dfa;
    font-size: 18px;
    margin-bottom: 16px;
  }

  .bill-to,
  .bill-from {
    margin-bottom: 24px;

    .input {
      margin-bottom: 8px;
    }

    .location-details {
      gap: 16px;

      div {
        flex: 1;
      }
    }
  }

  .payment {
    flex: 1;
    justify-content: flex-start;
    gap: 16px;
  }

  // item table
  .handle-items {
    .item-table {
      width: 100%;

      .table-heading,
      .table-data {
        gap: 16px;
        font-size: 14px;

        .item-name {
          flex-basis: 40%;
        }

        .quantity {
          flex-basis: 20%;
        }

        .price {
          flex-basis: 20%;
        }

        .total {
          flex-basis: 20%;
          justify-content: space-around;
          align-self: center;
        }
      }

      .table-heading {
        margin-bottom: 16px;

        th {
          text-align: center;
        }
      }

      .table-data {
        margin-bottom: 24px;

        .total {
          position: relative;

          span {
            max-width: 60px;
            overflow: scroll;
          }

          img {
            position: absolute;
            left: 80px;
            top: 2px;
            cursor: pointer;
            color: white;
          }
        }
      }
    }

    .button {
      color: #fff;
      background-color: #252945;
      align-items: center;
      justify-content: center;
      width: 60%;
      margin: 40px auto;
      padding: 12px 8px;
      font-size: 12px;

      svg {
        margin-right: 4px;
      }
    }
  }

  .save {
    margin-top: 30px;

    div {
      flex: 1;
    }

    .right {
      justify-content: flex-end;

      button {
        white-space: nowrap;
      }
    }
  }
}

.input {
  margin-bottom: 12px;
}

label {
  font-size: 14px;
  margin-bottom: 6px;
}

input,
select {
  width: 100%;
  background-color: #1e2139;
  color: #fff;
  border-radius: 4px;
  padding: 10px 6px;
  border: none;

  &:focus {
    outline: none;
    background-color: #282155;
  }

  &:disabled {
    background-color: #101d30;
    color: #777f98;
  }
}

hr {
  border: 0;
  padding-top: 10px;
  color: gray;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: inset 0 3px 3px -3px;
}
</style>
