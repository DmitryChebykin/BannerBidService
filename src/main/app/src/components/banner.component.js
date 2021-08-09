import React, {Component} from "react";
import BannerDataService from "../services/banner.service";
import _ from "lodash";

export default class Banner extends Component {
    constructor(props) {
        super(props);
        this.onChangeBannerName = this.onChangeBannerName.bind(this);
        this.onChangeContent = this.onChangeContent.bind(this);
        this.onChangeCategory = this.onChangeCategory.bind(this);
        this.onChangePrice = this.onChangePrice.bind(this);
        this.getTutorial = this.getTutorial.bind(this);
        this.updateDeleted = this.updateDeleted.bind(this);
        this.updateTutorial = this.updateTutorial.bind(this);

        this.state = {
            currentBanner: {
                id: null,
                bannerName: "",
                category: "",
                categoryName: "",
                content: "",
                deleted: false,

            },
            message: "",
            errors: ""

        };
    }

    componentDidMount() {
        this.getTutorial(this.props.match.params.id);
    }

    onChangeBannerName(e) {
        const bannerName = e.target.value;

        this.setState(function (prevState) {
            return {
                currentBanner: {
                    ...prevState.currentBanner,
                    bannerName: bannerName
                }
            };
        });
    }

    onChangeContent(e) {
        const content = e.target.value;

        this.setState(prevState => ({
            currentBanner: {
                ...prevState.currentBanner,
                content: content
            }
        }));
    }

    onChangeCategory(e) {
        const category = e.target.value;

        this.setState(prevState => ({
            currentBanner: {
                ...prevState.currentBanner,
                category: category
            }
        }));
    }

    onChangePrice(e) {
        const price = e.target.value;

        this.setState(prevState => ({
            currentBanner: {
                ...prevState.currentBanner,
                price: price
            }
        }));
    }

    getTutorial(id) {
        BannerDataService.get(id)
            .then(response => {
                this.setState({
                    currentBanner: response.data,
                });
                console.log(response.data);
            })
            .catch(e => {
                console.log(e);
            });
    }

    updateDeleted(status) {
        this.state.message ="";
        var data = {
            id: this.state.currentBanner.id,
            bannerName: this.state.currentBanner.bannerName,
            content: this.state.currentBanner.content,
            categoryName: _.get(this.state.currentBanner, 'category.categoryName'),
            deleted: status
        };

        BannerDataService.update(this.state.currentBanner.id, data)
            .then(response => {
                this.setState(prevState => ({
                    currentBanner: {
                        ...prevState.currentBanner,
                        deleted: status
                    }
                }));
                console.log(response.data);
            })
            .catch(e => {
                console.log(e);
            });
    }

    updateTutorial() {
        const string = "Баннер не обновлен   ";
        let cat = this.state.currentBanner.category;
        this.state.currentBanner.categoryName = cat;

        BannerDataService.update(
            this.state.currentBanner.id,
            this.state.currentBanner
        )
            .then(response => {
                console.log(response.data);
                if (response.data.errors !== undefined) {
                    this.setState({message: string +  JSON.stringify(response.data.errors )})
                } else {
                    this.setState({message: "The banner was updated successfully!"});
                }

            })
            .catch(e => {
                console.log(e);
            });
    }

    render() {
        const {currentBanner} = this.state;

        return (
            <div>
                {currentBanner ? (
                    <div className="edit-form">
                        <h4>Banner</h4>
                        <form>
                            <div className="form-group">
                                <label htmlFor="title">Title</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="title"
                                    value={currentBanner.bannerName}
                                    onChange={this.onChangeBannerName}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="description">Content</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="description"
                                    value={currentBanner.content}
                                    onChange={this.onChangeContent}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="category">Category</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="category"
                                    value={_.get(currentBanner, 'category.categoryName')}
                                    onChange={this.onChangeCategory}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="price">Price</label>
                                <input
                                    type="price"
                                    className="form-control"
                                    id="price"
                                    value={currentBanner.price}
                                    onChange={this.onChangePrice}
                                />
                            </div>


                            <div className="form-group">
                                <label>
                                    <strong>Deleted:</strong>
                                </label>
                                {currentBanner.deleted ? "Deleted" : "Active"}
                            </div>
                        </form>

                        {currentBanner.deleted ? (
                            <button
                                className="btn btn-outline-secondary"
                                onClick={() => this.updateDeleted(false)}
                            >
                                Activate
                            </button>
                        ) : (
                            <button
                                className="btn btn-outline-secondary"
                                onClick={() => this.updateDeleted(true)}
                            >
                                Deactivate
                            </button>
                        )}


                        <button
                            type="submit"
                            className="btn btn-outline-secondary"
                            onClick={this.updateTutorial}
                        >
                            Update
                        </button>
                        <p>{this.state.message}</p>
                    </div>
                ) : (
                    <div>
                        <br/>
                        <p>Please click on a Banner...</p>
                    </div>
                )}
            </div>
        );
    }
}