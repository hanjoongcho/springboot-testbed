import Styles from './index.css'
import $ from 'jquery'
import moment from 'moment'

$(function() {
    const _moment = moment() 
    $("strong > span").html(_moment.format('MMMM Do YYYY, h:mm:ss a'));
    console.log('AAFactory')
});