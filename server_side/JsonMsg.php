<?php
/**
 * Created by Alessandro Riccardi.
 * User: Alericcardi
 * Date: 25/07/2018
 * Time: 12:04
 */

class JsonMsg {
    // Generic response
    public static $NO_PARAM = "No enough parameters";

    /**
     * Print a json message.
     *
     * @param        $data
     * @param string $additional
     */
    public static function print_json($data, $additional = '') {
        $js_array = ['success' => true, 'data' => $data, 'additional' => $additional];

        echo json_encode($js_array);
    }

    /**
     * Print response messages.
     *
     * @param boolean $success
     * @param string  $msg
     * @param string  $data
     */
    public static function print_response($success, $msg = "", $data = '') {
        $js_array = ['success' => $success, 'msg' => $msg, 'additional' => $data];
        echo json_encode($js_array);
    }

}