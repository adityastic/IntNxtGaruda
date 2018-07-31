
<?php
function send_notification($body, $title, $type)
{
    define('API_ACCESS_KEY', 'AAAAFOJAD40:APA91bECvJJHNfkZHs_dZ-ZFG_FMezFNJenU2KUgd7h7vx70CmoRVJNM-ku5p-n3vfBLYkv6KxkfAik_oq-GUjuDFbrfZRiJI4YhmqwSOlm-8ZOyfR6HvYOhb4LK8sU14rNgDxZ3MxU59jNvnF9ftBvfd3Ki6sBKww');

    $msg = array
        (
        'body'  => $body,
        'title'  => $title
    );

    $dat = array
        (
        'type'  => $type
    );

    $fields = array
        (
        'to'           => '/topics/garudaNotifications',
        'notification' => $msg,
        'data'         => $dat,
    );

    $headers = array
        (
        'Authorization: key=' . API_ACCESS_KEY,
        'Content-Type: application/json',
    );

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
    $result = curl_exec($ch);
    echo $result;
    curl_close($ch);
}
?>