package game.Effect;

public interface ICanInflictEffect {
    StatusEffectInfo getEffect();
    void onEffectInflicted();
}
