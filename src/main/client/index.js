import Styles from './index.css'
import $ from 'jquery'
import moment from 'moment'

$(function() {
    const _moment = moment() 
    $("strong > span").html(_moment.format('MMMM Do YYYY, h:mm:ss a'));
    console.log('AAFactory')

    console.log(1)
    let myFirstPromise = new Promise((resolve, reject) => {
        console.log(2)
        // We call resolve(...) when what we were doing asynchronously was successful, and reject(...) when it failed.
        // In this example, we use setTimeout(...) to simulate async code.
        // In reality, you will probably be using something like XHR or an HTML5 API.
        setTimeout(function(){
            console.log(3)
            resolve("Success!"); // Yay! Everything went well!
        }, 250);
    });
    
    console.log(4)
    myFirstPromise.then((successMessage) => {
        console.log(5)
        // successMessage is whatever we passed in the resolve(...) function above.
        // It doesn't have to be a string, but if it is only a succeed message, it probably will be.
        console.log("Yay! " + successMessage);
    })
    .catch( function(error) { console.log(error) })
    .finally( function() { console.log(6) })
    
});