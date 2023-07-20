/**
 * plugins/vuetify.js
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'


// Composables
import { createVuetify } from 'vuetify'
import * as labsComponents from 'vuetify/labs/components'
import CDatePickerField from '../components/CDatePickerField'
import CCheckboxGroupHierarchy from '../components/CCheckboxGroupHierarchy'

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  components: {
    ...labsComponents,
    CDatePickerField,
    CCheckboxGroupHierarchy
  },
  theme: {
    themes: {
      light: {
        colors: {
          primary: '#1867C0',
          secondary: '#5CBBF6',
        },
      },
    },
  },
})
