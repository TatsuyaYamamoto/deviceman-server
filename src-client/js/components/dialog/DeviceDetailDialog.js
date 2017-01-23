import React from "react";
import ReactDOM from "react-dom";
import Dialog from "material-ui/Dialog";
import FlatButton from "material-ui/FlatButton";
import html2canvas from "html2canvas";

const QRCODE_IMAGE_BASE_URL = "http://chart.apis.google.com/chart?chs=150x150&cht=qr&chl=";

export default class DeviceDetailDialog extends React.Component {
    constructor(props) {
        super(props);
    };

    handleDownloadLabel = ()=> {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', QRCODE_IMAGE_BASE_URL + this.props.device.id, true);
        xhr.responseType = 'arraybuffer';

        const reader = new FileReader();
        reader.onload = ()=> {
            ReactDOM.findDOMNode(this.refs.qrcode).src = reader.result;
            html2canvas(this.refs.label, {
                onrendered: (canvas)=> {
                    window.location.href = canvas.toDataURL();
                }
            });
        };

        xhr.onload = ()=> {
            reader.readAsDataURL(new Blob([xhr.response], {type: "image/png"}));
        };
        xhr.send();
    };

    getLableComponent = () => {
        if (this.props.device != null) {
            return (<div ref="label" style={{textAlign: "center"}}>
                Torica<br/>
                <div>
                    <img width="100" src={"/torica/img/torica.png"}/>
                    <img
                        ref="qrcode"
                        src={QRCODE_IMAGE_BASE_URL + this.props.device.id}/><br/>
                </div>
                NAME: {this.props.device.name}<br/>
                IMEI: {this.props.device.id}<br/>
                MAC ADDRESS: {this.props.device.macAddress}
            </div>);
        }
    };

    render() {
        return (
            <Dialog
                style={{width: 450}}
                modal={false}
                open={this.props.isOpen}
                onRequestClose={()=> {
                    this.props.handleOpen(false)
                }}
                actions={[
                    <FlatButton
                        label="Download Lable"
                        primary={true}
                        onTouchTap={this.handleDownloadLabel}
                    />,
                    <FlatButton
                        label="Cancel"
                        primary={true}
                        onTouchTap={()=> {
                            this.props.handleOpen(false)
                        }}
                    />
                ]}>
                {this.props.isOpen && this.getLableComponent() }
            </Dialog>
        )
    }
}

DeviceDetailDialog.propTypes = {
    isOpen: React.PropTypes.bool.isRequired,
    device: React.PropTypes.object,
    handleOpen: React.PropTypes.func.isRequired
};
