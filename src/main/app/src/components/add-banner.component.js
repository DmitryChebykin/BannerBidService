import React, {Component} from "react";
import TutorialDataService from "../services/banner.service";

export default class AddBanner extends Component {
    constructor(props) {
        super(props);
        this.onChangeName = this.onChangeName.bind(this);
        this.onChangeContent = this.onChangeContent.bind(this);
        this.onChangeDeleted = this.onChangeDeleted.bind(this);
        this.onChangePrice = this.onChangePrice.bind(this);
        this.onChangeCategory = this.onChangeCategory.bind(this);
        this.saveTutorial = this.saveTutorial.bind(this);
        this.newTutorial = this.newTutorial.bind(this);

        this.state = {
            id: null,
            bannerName: "",
            content: "",
            category: "",
            deleted: false,
            submitted: false,
            messages: ""
        };
    }

    onChangeName(e) {
        this.setState({
            bannerName: e.target.value
        });
    }

    onChangeCategory(e) {
        this.setState({
            category: e.target.value  //_.get(banner, 'category.categoryName')
        });
    }

    onChangeDeleted(e) {
        this.setState({
            deleted: e.target.value
        });
    }

    onChangePrice(e) {
        this.setState({
            price: e.target.value
        });
    }

    onChangeContent(e) {
        this.setState({
            content: e.target.value
        });
    }

    saveTutorial() {
        var data = {
            bannerName: this.state.bannerName,
            price: this.state.price,
            categoryName: this.state.category,
            content: this.state.content,
            deleted: this.state.deleted,
        };

        TutorialDataService.create(data)
            .then(response => {
                this.setState({
                    id: response.data.id,
                    bannerName: response.data.bannerName,
                    price: response.data.price,
                    category: response.data.category,
                    content: response.data.content,
                    deleted: response.data.deleted,
                    messages: response.data.messages,
                    submitted: true
                });
                console.log(response.data);
            })
            .catch(e => {
                console.log(e);
            });
    }

    newTutorial() {
        this.setState({
            id: null,
            bannerName: "",
            content: "",
            category: "",
            price: "",
            deleted: false,
            submitted: false
        });
    }

    render() {
        if (this.state.submitted && this.state.messages !== "") this.state.error = true;
        return (
            <div className="submit-form">
                {this.state.submitted ? (
                    <div>
                        {this.state.error ? (<h4>Баннер не добавлен <br/> + {this.state.messages}</h4>) :
                            (<h4>You submitted successfully!</h4>)}
                        <button className="btn btn-success" onClick={this.newTutorial}>
                            Add
                        </button>
                    </div>
                ) : (
                    <div>
                        <div className="form-group">
                            <label htmlFor="title">Name</label>
                            <input
                                type="text"
                                className="form-control"
                                id="title"
                                required
                                value={this.state.bannerName}
                                onChange={this.onChangeName}
                                name="bannerName"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="content">Content</label>
                            <input
                                type="text"
                                className="form-control"
                                id="content"
                                required
                                value={this.state.content}
                                onChange={this.onChangeContent}
                                name="content"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="category">Category</label>
                            <input
                                type="text"
                                className="form-control"
                                id="category"
                                required
                                value={this.state.category}
                                onChange={this.onChangeCategory}
                                name="category"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="price">Price</label>
                            <input
                                type="text"
                                className="form-control"
                                id="price"
                                required
                                value={this.state.price}
                                onChange={this.onChangePrice}
                                name="price"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="deleted">Deleted</label>
                            <input
                                type="text"
                                className="form-control"
                                id="deleted"
                                required
                                value={this.state.deleted}
                                onChange={this.onChangeDeleted}
                                name="deleted"
                            />
                        </div>

                        <button onClick={this.saveTutorial} className="btn btn-success">
                            Submit
                        </button>
                    </div>
                )}
            </div>
        );
    }
}