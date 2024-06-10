import {createStore} from "vuex";

export default createStore({
  state: {
    invoiceData: [],
    invoiceModal: null,
    modal: null,
    invoiceLoaded: null,
    currentInvoice: null,
    edit: null,
  },
  mutations: {
    TOGGLE_INVOICE_MODAL(state) {
      state.invoiceModal = !state.invoiceModal;
    },

    TOGGLE_MODAL(state) {
      state.modal = !state.modal;
    },

    SET_INVOICE_DATA(state, payload) {
      state.invoiceData.push(payload);
    },

    INVOICES_LOADED(state) {
      state.invoiceLoaded = true;
    },

    async SET_CURRENT_INVOICE(state, payload) {
      // called when a particular invoice is selected
      state.currentInvoice = state.invoiceData.find(
        (invoice) => invoice.id === Number(payload)
      );
      const response = await fetch(
        `http://localhost:8090/api/v1/invoices/${payload}`
      );
      const data = await response.json();
      state.currentInvoice.invoiceItemList = data.products;
      state.currentInvoice.total = data.total;
    },

    TOGGLE_EDIT(state) {
      state.edit = !state.edit;
    },

    DELETE_INVOICE(state, payload) {
      state.invoiceData = state.invoiceData.filter(
        (invoice) => invoice.docId !== payload
      );
    },

    UPDATE_TO_PAID(state, payload) {
      state.invoiceData.forEach((invoice) => {
        if (invoice.id === Number(payload)) {
          invoice.invoicePending = false;
          invoice.invoicePaid = true;
        }
      });
    },

    UPDATE_TO_PENDING(state, payload) {
      state.invoiceData.forEach((invoice) => {
        if (invoice.id === payload) {
          invoice.invoicePending = true;
          invoice.invoicePaid = false;
          invoice.invoiceDraft = false;
        }
      });
    },
  },

  actions: {
    async GET_INVOICES({ commit, state }) {
      // get data from http://localhost:8090/api/v1/invoices asynchronously
      const response = await fetch("http://localhost:8090/api/v1/invoices");
      const data = await response.json();
      console.log("Data: ", data);
      data.forEach((invoice) => {
        if (
          !state.invoiceData.some((invoice) => invoice.docId === invoice.id)
        ) {
          const data = {
            id: invoice.id,
            invoiceId: invoice.invoiceId,
            clientName: invoice.clientName,
            clientEmail: invoice.clientEmail,
            clientStreetAddress: invoice.clientStreetAddress,
            clientCity: invoice.clientCity,
            clientZipCode: invoice.clientZipCode,
            clientCountry: invoice.clientCountry,
            paymentDueDate: invoice.dueDate,
            invoiceDate: invoice.createdAt.split(" ")[0],
            invoiceNotes: invoice.notes,
            invoiceStatus: invoice.invoiceStatus,
            invoiceTotal: invoice.total,
            invoicePending: invoice.invoiceStatus === "PENDING",
            invoiceDraft: invoice.invoiceStatus === "DRAFT",
            invoicePaid: invoice.invoiceStatus === "PAID",
            invoiceItemList: invoice.invoiceItemList,
          };
          commit("SET_INVOICE_DATA", data);
        }
      });
      commit("INVOICES_LOADED");
    },

    async UPDATE_INVOICE({ commit, dispatch }, { docId, routeId }) {
      console.log(
        "Commit: ",
        commit,
        "Dispatch: ",
        dispatch,
        "DocId: ",
        docId,
        "RouteId: ",
        routeId
      );
    },

    async DELETE_INVOICE({ commit }, invoiceId) {
      commit; // removing this is problematic to the linter
      await fetch(`http://localhost:8090/api/v1/invoices/${invoiceId}`, {
        method: "DELETE",
      });
    },

    async UPDATE_TO_PAID({ commit }, invoiceID) {
      console.log("Commit: ", commit, "DocId: ", invoiceID);
      await fetch(`http://localhost:8090/api/v1/invoices/${invoiceID}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          invoiceStatus: "PAID",
        }),
      }).finally(() => {
        window.location.reload();
      });
    },

    async UPDATE_TO_PENDING({ commit }, invoiceID) {
      console.log("185: Commit: ", commit, "DocId: ", invoiceID);
      await fetch(`http://localhost:8090/api/v1/invoices/${invoiceID}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          invoiceStatus: "PENDING",
        }),
      }).finally(() => {
        window.location.reload();
      });
    },
  },
  modules: {},
});
